package co.com.fcv.training.citas.adapter.web;

import co.com.fcv.training.citas.application.SchedulingService;
import co.com.fcv.training.citas.domain.Scheduling;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
class SchedulingController {
 private final SchedulingService scheduling;
 SchedulingController(SchedulingService s){scheduling=s;}

 record CatalogLocation(long id,String code,String name){}
 record CatalogSpecialty(long id,String code,String name,int durationMinutes,boolean general,boolean requiresAdminApproval){}
 record ProfessionalResponse(long id,String code,String name){}
 record SlotResponse(long professionalId,long locationId,LocalDateTime startAt,LocalDateTime endAt){}
 record ReserveRequest(@Positive long professionalId,@Positive long locationId,@Positive long specialtyId,@NotNull LocalDateTime startAt,@Size(max=500) String reason){}
 record AppointmentResponse(long id,String status,long professionalId,long locationId,long specialtyId,LocalDateTime startAt,LocalDateTime endAt,String reason){}
 record DecisionRequest(@NotBlank @Pattern(regexp="APPROVE|REJECT") String decision,@Size(max=500) String reason){}
 record BlockRequest(@Positive long locationId,@NotNull LocalDate date,@NotNull LocalTime start,@NotNull LocalTime end){}

 @GetMapping("/catalogs/locations") List<CatalogLocation> locations(){return scheduling.locations().stream().map(x->new CatalogLocation(x.id(),x.code(),x.name())).toList();}
 @GetMapping("/catalogs/specialties") List<CatalogSpecialty> specialties(){return scheduling.specialties().stream().map(x->new CatalogSpecialty(x.id(),x.code(),x.name(),x.durationMinutes(),x.general(),x.approvalRequired())).toList();}
 @GetMapping("/availability") @PreAuthorize("hasRole('USER')")
 List<SlotResponse> availability(@RequestParam long specialtyId,@RequestParam long locationId,@RequestParam(required=false) Long professionalId,@RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date){return scheduling.availability(specialtyId,locationId,professionalId,date).stream().map(x->new SlotResponse(x.professionalId(),x.locationId(),x.startAt(),x.endAt())).toList();}
 @GetMapping("/professionals") @PreAuthorize("hasRole('USER')")
 List<ProfessionalResponse> professionals(@RequestParam long specialtyId,@RequestParam long locationId){return scheduling.professionals(specialtyId,locationId).stream().map(x->new ProfessionalResponse(x.id(),x.code(),x.name())).toList();}
 @PostMapping("/appointments") @PreAuthorize("hasRole('USER')") ResponseEntity<AppointmentResponse> reserve(@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody ReserveRequest r){Scheduling.Appointment a=scheduling.reserve(user(jwt),new SchedulingService.Reservation(r.professionalId(),r.locationId(),r.specialtyId(),r.startAt(),r.reason()));return ResponseEntity.status(HttpStatus.CREATED).body(response(a));}
 @PostMapping("/admin/appointments/{id}/decision") @PreAuthorize("hasRole('ADMIN')") ResponseEntity<Void> decision(@AuthenticationPrincipal Jwt jwt,@PathVariable long id,@Valid @RequestBody DecisionRequest r){scheduling.decide(user(jwt),id,new SchedulingService.Decision(r.decision(),r.reason()));return ResponseEntity.noContent().build();}
 @PostMapping("/professional/availability-blocks") @PreAuthorize("hasRole('PROFESSIONAL')") ResponseEntity<Void> createBlock(@AuthenticationPrincipal Jwt jwt,@Valid @RequestBody BlockRequest r){scheduling.createBlock(user(jwt),new SchedulingService.Block(r.locationId(),r.date(),r.start(),r.end()));return ResponseEntity.status(HttpStatus.CREATED).build();}
 private long user(Jwt jwt){return Long.parseLong(jwt.getSubject());}
 private AppointmentResponse response(Scheduling.Appointment a){return new AppointmentResponse(a.id(),a.status(),a.professionalId(),a.locationId(),a.specialtyId(),a.startAt(),a.endAt(),a.reason());}
}
