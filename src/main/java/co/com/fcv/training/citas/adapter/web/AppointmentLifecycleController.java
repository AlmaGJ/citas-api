package co.com.fcv.training.citas.adapter.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.*;

@RestController
class AppointmentLifecycleController {
    record Appointment(long id, long patientUserId, long professionalId, long locationId, long specialtyId,
                       String specialty, String professional, String location, int durationMinutes,
                       String status, LocalDateTime scheduledStartAt, LocalDateTime scheduledEndAt, String reason) {}
    record CancelRequest(@Size(max = 500) String reason) {}
    record RescheduleRequest(@NotNull LocalDateTime startAt, @NotNull Long locationId) {}
    record Decision(@NotBlank String decision, @Size(max = 500) String reason) {}
    record Closure(@NotBlank String status, @Size(max = 500) String reason) {}

    private final JdbcTemplate jdbc;
    private final Clock clock;
    private final AppointmentEventPublisher events;
    AppointmentLifecycleController(JdbcTemplate jdbc, Clock clock, AppointmentEventPublisher events) { this.jdbc = jdbc; this.clock = clock; this.events = events; }

    @GetMapping("/api/v1/appointments") @PreAuthorize("hasRole('USER')")
    List<Appointment> mine(@AuthenticationPrincipal Jwt jwt, @RequestParam(required=false) String status,
                           @RequestParam(required=false) LocalDate from, @RequestParam(required=false) LocalDate to) {
        StringBuilder sql = new StringBuilder(baseQuery()).append(" where a.patient_user_id=?");
        List<Object> args = new ArrayList<>(List.of(userId(jwt)));
        if (status != null && !status.isBlank()) { sql.append(" and s.code=?"); args.add(status); }
        if (from != null) { sql.append(" and a.scheduled_start_at>=?"); args.add(from.atStartOfDay()); }
        if (to != null) { sql.append(" and a.scheduled_start_at<?"); args.add(to.plusDays(1).atStartOfDay()); }
        sql.append(" order by a.scheduled_start_at desc");
        return jdbc.query(sql.toString(), this::row, args.toArray());
    }

    @GetMapping("/api/v1/appointments/{id}") @PreAuthorize("hasRole('USER')")
    Appointment detail(@AuthenticationPrincipal Jwt jwt, @PathVariable long id) {
        return jdbc.queryForObject(baseQuery()+" where a.id=? and a.patient_user_id=?", this::row, id, userId(jwt));
    }

    @PostMapping("/api/v1/appointments/{id}/cancel") @PreAuthorize("hasRole('USER')") @Transactional
    ResponseEntity<Appointment> cancel(@AuthenticationPrincipal Jwt jwt, @PathVariable long id,
                                       @RequestBody(required=false) CancelRequest body) {
        Appointment a = detail(jwt,id);
        if (a.scheduledStartAt().isBefore(LocalDateTime.now(clock)) || Set.of("CANCELLED","REJECTED","COMPLETED","NO_SHOW").contains(a.status()))
            throw new IllegalArgumentException("La cita no se puede cancelar");
        updateStatus(id, "CANCELLED", userId(jwt), "USER", body == null ? null : body.reason());
        jdbc.update("update professional_slots set appointment_id=null where appointment_id=?", id);
        events.publish(id, "CANCELLED", "USER", userId(jwt));
        return ResponseEntity.ok(detail(jwt,id));
    }

    @GetMapping("/api/v1/appointments/{id}/history") @PreAuthorize("isAuthenticated()")
    List<Map<String,Object>> history(@AuthenticationPrincipal Jwt jwt, @PathVariable long id) {
        Appointment a = jdbc.queryForObject(baseQuery()+" where a.id=?", this::row, id);
        if (!isAdmin(jwt) && a.patientUserId()!=userId(jwt) && !professionalOwns(jwt,a.professionalId())) throw new org.springframework.security.access.AccessDeniedException("Sin acceso");
        return jdbc.queryForList("select h.id, s.code status, h.change_source source, h.changed_by_user_id actorUserId, h.changed_at changedAt, h.reason from appointment_status_history h join appointment_statuses s on s.id=h.status_id where h.appointment_id=? order by h.changed_at", id);
    }

    @PostMapping("/api/v1/appointments/{id}/reschedule-requests") @PreAuthorize("hasRole('USER')") @Transactional
    ResponseEntity<Map<String,Object>> requestReschedule(@AuthenticationPrincipal Jwt jwt, @PathVariable long id, @Valid @RequestBody RescheduleRequest body) {
        Appointment a = detail(jwt,id);
        if (!"APPROVED".equals(a.status()) || !a.scheduledStartAt().isAfter(LocalDateTime.now(clock))) throw new IllegalArgumentException("La cita no admite reprogramación");
        Integer pending = jdbc.queryForObject("select count(*) from reschedule_requests r join reschedule_request_statuses s on s.id=r.status_id where r.appointment_id=? and s.code='PENDING'", Integer.class, id);
        if (pending != null && pending > 0) throw new IllegalArgumentException("Ya existe una solicitud pendiente");
        LocalDateTime end = body.startAt().plusMinutes(a.durationMinutes());
        int expected=a.durationMinutes()/30;
        List<Long> free = jdbc.query("select ps.id from professional_slots ps join availability_blocks b on b.id=ps.availability_block_id where b.professional_id=? and b.location_id=? and ps.start_at>=? and ps.start_at<? and ps.appointment_id is null order by ps.start_at for update", (rs,n)->rs.getLong(1), a.professionalId(), body.locationId(), body.startAt(), end);
        if (free.size()!=expected) throw new IllegalArgumentException("La nueva franja no está disponible");
        Long statusId = jdbc.queryForObject("select id from reschedule_request_statuses where code='PENDING'", Long.class);
        jdbc.update("insert into reschedule_requests(appointment_id,requested_by_user_id,requested_location_id,status_id,previous_start_at,previous_end_at,requested_start_at,requested_end_at) values (?,?,?,?,?,?,?,?)", id,userId(jwt),body.locationId(),statusId,a.scheduledStartAt(),a.scheduledEndAt(),body.startAt(),end);
        Long reqId = jdbc.queryForObject("select last_insert_id()", Long.class);
        jdbc.update("update professional_slots set appointment_id=? where id in ("+placeholders(free.size())+")", concat(id,free));
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", reqId == null ? 0 : reqId, "status", "PENDING"));
    }

    @GetMapping("/api/v1/appointments/{id}/reschedule-requests") @PreAuthorize("hasRole('USER')")
    List<Map<String,Object>> rescheduleRequests(@AuthenticationPrincipal Jwt jwt, @PathVariable long id) {
        detail(jwt, id);
        return jdbc.queryForList("select r.id, rs.code status, r.requested_location_id requestedLocationId, r.previous_start_at previousStartAt, r.previous_end_at previousEndAt, r.requested_start_at requestedStartAt, r.requested_end_at requestedEndAt, r.decision_reason decisionReason, r.decided_at decidedAt, r.created_at createdAt from reschedule_requests r join reschedule_request_statuses rs on rs.id=r.status_id where r.appointment_id=? order by r.created_at desc", id);
    }

    @GetMapping("/api/v1/professional/appointments") @PreAuthorize("hasRole('PROFESSIONAL')")
    List<Appointment> professional(@AuthenticationPrincipal Jwt jwt, @RequestParam LocalDate from, @RequestParam LocalDate to,
                                   @RequestParam(required=false) Long locationId) {
        String sql=baseQuery()+" where p.user_id=? and s.code='APPROVED' and a.scheduled_start_at>=? and a.scheduled_start_at<?";
        List<Object> args=new ArrayList<>(List.of(userId(jwt),from.atStartOfDay(),to.plusDays(1).atStartOfDay()));
        if(locationId!=null){sql+=" and a.location_id=?";args.add(locationId);} sql+=" order by a.scheduled_start_at";
        return jdbc.query(sql,this::row,args.toArray());
    }

    @PostMapping("/api/v1/professional/appointments/{id}/closure") @PreAuthorize("hasRole('PROFESSIONAL')") @Transactional
    Appointment closure(@AuthenticationPrincipal Jwt jwt,@PathVariable long id,@Valid @RequestBody Closure body){
        Appointment a=jdbc.queryForObject(baseQuery()+" where a.id=? and p.user_id=?",this::row,id,userId(jwt));
        if(!"APPROVED".equals(a.status()) || a.scheduledEndAt().isAfter(LocalDateTime.now(clock))) throw new IllegalArgumentException("La cita aún no puede cerrarse");
        if(!Set.of("COMPLETED","NO_SHOW").contains(body.status())) throw new IllegalArgumentException("Estado de cierre inválido");
        updateStatus(id,body.status(),userId(jwt),"PROFESSIONAL",body.reason()); return jdbc.queryForObject(baseQuery()+" where a.id=?",this::row,id);
    }

    @GetMapping("/api/v1/admin/appointments/upcoming") @PreAuthorize("hasRole('ADMIN')")
    List<Appointment> upcoming(@RequestParam LocalDate from,@RequestParam LocalDate to,@RequestParam(required=false) Long locationId){
        String sql=baseQuery()+" where s.code='APPROVED' and a.scheduled_start_at>=? and a.scheduled_start_at<?"; List<Object> args=new ArrayList<>(List.of(from.atStartOfDay(),to.plusDays(1).atStartOfDay()));
        if(locationId!=null){sql+=" and a.location_id=?";args.add(locationId);} return jdbc.query(sql+" order by a.scheduled_start_at",this::row,args.toArray());
    }

    @GetMapping("/api/v1/admin/inbox") @PreAuthorize("hasRole('ADMIN')")
    List<Map<String,Object>> inbox(@RequestParam(required=false) Long locationId, @RequestParam(required=false) Long professionalId,
                                   @RequestParam(required=false) Long specialtyId, @RequestParam(required=false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date) {
        StringBuilder appointmentsSql = new StringBuilder("select 'APPOINTMENT' type, a.id, s.code status, a.professional_id professionalId, a.location_id locationId, a.specialty_id specialtyId, sp.name specialty, concat(u.first_name,' ',u.last_name) professional, l.name location, a.scheduled_start_at scheduledStartAt, a.scheduled_end_at scheduledEndAt, a.reason from appointments a join appointment_statuses s on s.id=a.status_id join specialties sp on sp.id=a.specialty_id join professionals p on p.id=a.professional_id join users u on u.id=p.user_id join locations l on l.id=a.location_id where s.code='REQUESTED'");
        List<Object> appointmentArgs = new ArrayList<>();
        if (locationId != null) { appointmentsSql.append(" and a.location_id=?"); appointmentArgs.add(locationId); }
        if (professionalId != null) { appointmentsSql.append(" and a.professional_id=?"); appointmentArgs.add(professionalId); }
        if (specialtyId != null) { appointmentsSql.append(" and a.specialty_id=?"); appointmentArgs.add(specialtyId); }
        if (date != null) { appointmentsSql.append(" and a.scheduled_start_at>=? and a.scheduled_start_at<?"); appointmentArgs.add(date.atStartOfDay()); appointmentArgs.add(date.plusDays(1).atStartOfDay()); }

        StringBuilder rescheduleSql = new StringBuilder("select 'RESCHEDULE' type, r.id, rs.code status, ap.professional_id professionalId, r.requested_location_id locationId, ap.specialty_id specialtyId, sp.name specialty, concat(u.first_name,' ',u.last_name) professional, l.name location, r.requested_start_at scheduledStartAt, r.requested_end_at scheduledEndAt, null reason from reschedule_requests r join reschedule_request_statuses rs on rs.id=r.status_id join appointments ap on ap.id=r.appointment_id join specialties sp on sp.id=ap.specialty_id join professionals p on p.id=ap.professional_id join users u on u.id=p.user_id join locations l on l.id=r.requested_location_id where rs.code='PENDING'");
        List<Object> rescheduleArgs = new ArrayList<>();
        if (locationId != null) { rescheduleSql.append(" and r.requested_location_id=?"); rescheduleArgs.add(locationId); }
        if (professionalId != null) { rescheduleSql.append(" and ap.professional_id=?"); rescheduleArgs.add(professionalId); }
        if (specialtyId != null) { rescheduleSql.append(" and ap.specialty_id=?"); rescheduleArgs.add(specialtyId); }
        if (date != null) { rescheduleSql.append(" and r.requested_start_at>=? and r.requested_start_at<?"); rescheduleArgs.add(date.atStartOfDay()); rescheduleArgs.add(date.plusDays(1).atStartOfDay()); }

        List<Object> args = new ArrayList<>(appointmentArgs);
        args.addAll(rescheduleArgs);
        String sql = appointmentsSql + " union all " + rescheduleSql + " order by scheduledStartAt";
        return jdbc.queryForList(sql, args.toArray());
    }

    @PostMapping("/api/v1/admin/reschedule-requests/{id}/decision") @PreAuthorize("hasRole('ADMIN')") @Transactional
    ResponseEntity<Void> rescheduleDecision(@AuthenticationPrincipal Jwt jwt,@PathVariable long id,@Valid @RequestBody Decision body){
        var r=jdbc.queryForMap("select r.*,rs.code status,a.scheduled_start_at current_start,a.scheduled_end_at current_end from reschedule_requests r join reschedule_request_statuses rs on rs.id=r.status_id join appointments a on a.id=r.appointment_id where r.id=?",id);
        if(!"PENDING".equals(r.get("status"))) throw new IllegalArgumentException("Solicitud no pendiente");
        if("REJECT".equals(body.decision()) && (body.reason()==null||body.reason().isBlank())) throw new IllegalArgumentException("El rechazo requiere motivo");
        long appointment=((Number)r.get("appointment_id")).longValue(); String next="APPROVE".equals(body.decision())?"APPROVED":"REJECTED";
        if("APPROVED".equals(next)){ jdbc.update("update professional_slots set appointment_id=null where appointment_id=? and start_at=?",appointment,r.get("current_start")); jdbc.update("update appointments set location_id=?,scheduled_start_at=?,scheduled_end_at=? where id=?",r.get("requested_location_id"),r.get("requested_start_at"),r.get("requested_end_at"),appointment); }
        else jdbc.update("update professional_slots set appointment_id=null where appointment_id=? and start_at=?",appointment,r.get("requested_start_at"));
        jdbc.update("update reschedule_requests set status_id=(select id from reschedule_request_statuses where code=?),decision_reason=?,decided_by_user_id=?,decided_at=? where id=?",next,body.reason(),userId(jwt),LocalDateTime.now(clock),id);
        events.publishRescheduleDecision(appointment,id,next,userId(jwt));
        return ResponseEntity.noContent().build();
    }

    private String baseQuery(){return "select a.id,a.patient_user_id,a.professional_id,a.location_id,a.specialty_id,sp.name specialty,concat(u.first_name,' ',u.last_name) professional,l.name location,sp.appointment_duration_minutes durationMinutes,s.code status,a.scheduled_start_at,a.scheduled_end_at,a.reason from appointments a join appointment_statuses s on s.id=a.status_id join specialties sp on sp.id=a.specialty_id join professionals p on p.id=a.professional_id join users u on u.id=p.user_id join locations l on l.id=a.location_id";}
    private Appointment row(java.sql.ResultSet rs,int n)throws java.sql.SQLException{return new Appointment(rs.getLong("id"),rs.getLong("patient_user_id"),rs.getLong("professional_id"),rs.getLong("location_id"),rs.getLong("specialty_id"),rs.getString("specialty"),rs.getString("professional"),rs.getString("location"),rs.getInt("durationMinutes"),rs.getString("status"),rs.getTimestamp("scheduled_start_at").toLocalDateTime(),rs.getTimestamp("scheduled_end_at").toLocalDateTime(),rs.getString("reason"));}
    private void updateStatus(long id,String status,long actor,String source,String reason){ jdbc.update("update appointments set status_id=(select id from appointment_statuses where code=?) where id=?",status,id); jdbc.update("insert into appointment_status_history(appointment_id,status_id,changed_by_user_id,change_source,reason) values (?,(select id from appointment_statuses where code=?),?,?,?)",id,status,actor,source,reason); }
    private long userId(Jwt jwt){return Long.parseLong(jwt.getSubject());}
    private boolean isAdmin(Jwt jwt){return jwt.getClaimAsStringList("roles")!=null&&jwt.getClaimAsStringList("roles").contains("ADMIN");}
    private boolean professionalOwns(Jwt jwt,long professionalId){Integer x=jdbc.queryForObject("select count(*) from professionals where id=? and user_id=?",Integer.class,professionalId,userId(jwt));return x!=null&&x>0;}
    private String placeholders(int n){return String.join(",",Collections.nCopies(n,"?"));}
    private Object[] concat(long id,List<Long> ids){Object[] x=new Object[ids.size()+1];x[0]=id;for(int i=0;i<ids.size();i++)x[i+1]=ids.get(i);return x;}
}
