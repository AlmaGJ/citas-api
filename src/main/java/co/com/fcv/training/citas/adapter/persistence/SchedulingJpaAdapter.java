package co.com.fcv.training.citas.adapter.persistence;

import co.com.fcv.training.citas.application.*;
import co.com.fcv.training.citas.domain.Scheduling;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Repository
class SchedulingJpaAdapter implements Ports.SchedulingPort {
 private final LocationsJpa locations; private final SpecialtiesJpa specialties; private final ProfessionalsJpa professionals;
 private final BlocksJpa blocks; private final SlotsJpa slots; private final AppointmentsJpa appointments;
 private final AppointmentStatusesJpa statuses; private final AppointmentHistoryJpa history; private final Clock clock;
 SchedulingJpaAdapter(LocationsJpa l, SpecialtiesJpa s, ProfessionalsJpa p, BlocksJpa b, SlotsJpa slots, AppointmentsJpa a, AppointmentStatusesJpa st, AppointmentHistoryJpa h, Clock c) {
  locations=l; specialties=s; professionals=p; blocks=b; this.slots=slots; appointments=a; statuses=st; history=h; clock=c;
 }
 public List<Scheduling.Location> locations(){return locations.findByActiveTrueOrderByName().stream().map(e->new Scheduling.Location(e.id.longValue(),e.code,e.name,e.active)).toList();}
 public List<Scheduling.Specialty> specialties(){return specialties.findByActiveTrueOrderByName().stream().map(this::specialty).toList();}
 public Scheduling.Specialty specialty(long id){return specialty(specialties.findById((short)id).orElseThrow(()->notFound("Especialidad no encontrada")));}
 private Scheduling.Specialty specialty(SpecialtyEntity e){return new Scheduling.Specialty(e.id.longValue(),e.code,e.name,e.duration,e.general,e.approvalRequired,e.active);}
 public List<Scheduling.Professional> professionals(long specialtyId,long locationId){return professionals.availableFor(specialtyId,locationId).stream().map(e->new Scheduling.Professional(e.id,e.userId,e.code,e.code,e.active)).toList();}
 public boolean canAttend(long p,long l,long s){return locations.findById((short)l).filter(x->x.active).isPresent() && professionals.canAttend(p,l,s);}
 public List<Scheduling.Slot> available(long specialtyId,long locationId,Long professionalId,LocalDate date,int minutes){
  List<ProfessionalEntity> candidates=professionalId==null?professionals.availableFor(specialtyId,locationId):professionals.findById(professionalId).filter(p->canAttend(p.id,locationId,specialtyId)).stream().toList();
  LocalDateTime from=date.atStartOfDay(), to=from.plusDays(1); int required=minutes/30; List<Scheduling.Slot> result=new ArrayList<>();
  for(ProfessionalEntity p:candidates){List<ProfessionalSlotEntity> free=slots.free(p.id,locationId,from,to); for(int i=0;i+required<=free.size();i++){boolean consecutive=true; for(int j=1;j<required;j++) if(!free.get(i+j).startAt.equals(free.get(i).startAt.plusMinutes(30L*j))) consecutive=false; if(consecutive) result.add(new Scheduling.Slot(p.id,locationId,free.get(i).startAt,free.get(i).startAt.plusMinutes(minutes)));}}
  return result;
 }
 @Transactional
 public Scheduling.Appointment reserve(long patientId,SchedulingService.Reservation c,int duration,String status,String source){
  LocalDateTime end=c.startAt().plusMinutes(duration); List<ProfessionalSlotEntity> locked=slots.lockRange(c.professionalId(),c.locationId(),c.startAt(),end); int expected=duration/30;
  if(locked.size()!=expected || locked.stream().anyMatch(s->s.appointmentId!=null) || !consecutive(locked,c.startAt())) throw conflict("El horario ya no está disponible");
  AppointmentEntity a=new AppointmentEntity(); a.patientId=patientId;a.professionalId=c.professionalId();a.locationId=(short)c.locationId();a.specialtyId=(short)c.specialtyId();a.statusId=status(status).id;a.reason=c.reason();a.startAt=c.startAt();a.endAt=end;a.createdBy=patientId;a.createdAt=clock.instant();
  if("APPROVED".equals(status)){a.approvedBy=null;a.approvedAt=LocalDateTime.now(clock);} a=appointments.saveAndFlush(a); for(ProfessionalSlotEntity s:locked)s.appointmentId=a.id; slots.saveAll(locked); audit(a.id,a.statusId,"SYSTEM".equals(source)?null:patientId,source,c.reason()); return appointment(a,status);
 }
 @Transactional
 public void decide(long adminId,long id,String decision,String reason){ AppointmentEntity a=appointments.findById(id).orElseThrow(()->notFound("Cita no encontrada")); if(a.statusId.shortValue()!=status("REQUESTED").id.shortValue()) throw conflict("La cita no está pendiente"); String next="APPROVE".equals(decision)?"APPROVED":"REJECTED"; a.statusId=status(next).id; a.approvedBy=adminId;a.approvedAt=LocalDateTime.now(clock); appointments.save(a); if("REJECTED".equals(next)){List<ProfessionalSlotEntity> assigned=slots.findByAppointmentId(id); assigned.forEach(s->s.appointmentId=null); slots.saveAll(assigned);} audit(id,a.statusId,adminId,"ADMIN",reason); }
 @Transactional
 public void createBlock(long professionalUserId,SchedulingService.Block c){ ProfessionalEntity p=professionals.findByUserId(professionalUserId).filter(x->x.active).orElseThrow(()->notFound("Profesional no habilitado")); if(!locations.findById((short)c.locationId()).filter(x->x.active).isPresent() || !professionals.hasLocation(p.id,c.locationId())) throw invalid("La sede no está asignada al profesional"); if(!blocks.overlapping(p.id,c.date(),c.start(),c.end()).isEmpty()) throw conflict("El bloque se solapa con otro"); AvailabilityBlockEntity b=new AvailabilityBlockEntity();b.professionalId=p.id;b.locationId=(short)c.locationId();b.date=c.date();b.start=c.start();b.end=c.end();b=blocks.saveAndFlush(b);List<ProfessionalSlotEntity> generated=new ArrayList<>(); for(LocalDateTime t=c.date().atTime(c.start());t.isBefore(c.date().atTime(c.end()));t=t.plusMinutes(30)){ProfessionalSlotEntity s=new ProfessionalSlotEntity();s.blockId=b.id;s.startAt=t;s.endAt=t.plusMinutes(30);generated.add(s);}slots.saveAll(generated); }
 private boolean consecutive(List<ProfessionalSlotEntity> xs,LocalDateTime start){for(int i=0;i<xs.size();i++)if(!xs.get(i).startAt.equals(start.plusMinutes(30L*i)))return false;return true;}
 private AppointmentStatusEntity status(String code){return statuses.findByCode(code).orElseThrow(()->new IllegalStateException("Estado fijo ausente"));}
 private void audit(long appointment,short status,long actor,String source,String reason){AppointmentHistoryEntity h=new AppointmentHistoryEntity();h.appointmentId=appointment;h.statusId=status;h.actorId=actor;h.source=source;h.reason=reason;h.changedAt=clock.instant();history.save(h);}
 private Scheduling.Appointment appointment(AppointmentEntity a,String code){return new Scheduling.Appointment(a.id,a.patientId,a.professionalId,a.locationId.longValue(),a.specialtyId.longValue(),code,a.startAt,a.endAt,a.reason);}
 private SchedulingFailure conflict(String m){return new SchedulingFailure(SchedulingFailure.Kind.CONFLICT,m);} private SchedulingFailure invalid(String m){return new SchedulingFailure(SchedulingFailure.Kind.INVALID,m);} private SchedulingFailure notFound(String m){return new SchedulingFailure(SchedulingFailure.Kind.NOT_FOUND,m);}
}
