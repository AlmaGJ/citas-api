package co.com.fcv.training.citas.adapter.persistence;

import co.com.fcv.training.citas.application.*;
import co.com.fcv.training.citas.domain.Scheduling;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.*;

@Repository
class SchedulingJpaAdapter implements Ports.SchedulingPort {
 private final LocationsJpa locations; private final SpecialtiesJpa specialties; private final ProfessionalsJpa professionals;
 private final BlocksJpa blocks; private final SlotsJpa slots; private final AppointmentsJpa appointments;
 private final AppointmentStatusesJpa statuses; private final AppointmentHistoryJpa history; private final Clock clock;
 private final UsersJpa users; private final ProfessionalSpecialtiesJpa professionalSpecialties; private final ProfessionalLocationsJpa professionalLocations;
 SchedulingJpaAdapter(LocationsJpa l, SpecialtiesJpa s, ProfessionalsJpa p, BlocksJpa b, SlotsJpa slots, AppointmentsJpa a, AppointmentStatusesJpa st, AppointmentHistoryJpa h, Clock c,
                      UsersJpa users, ProfessionalSpecialtiesJpa professionalSpecialties, ProfessionalLocationsJpa professionalLocations) {
  locations=l; specialties=s; professionals=p; blocks=b; this.slots=slots; appointments=a; statuses=st; history=h; clock=c;
  this.users=users; this.professionalSpecialties=professionalSpecialties; this.professionalLocations=professionalLocations;
 }
 public List<Scheduling.Location> locations(){return locations.findByActiveTrueOrderByName().stream().map(e->new Scheduling.Location(e.id.longValue(),e.code,e.name,e.active)).toList();}
 public List<Scheduling.Specialty> specialties(){return specialties.findByActiveTrueOrderByName().stream().map(this::specialty).toList();}
 public Scheduling.Specialty specialty(long id){return specialty(specialties.findById((short)id).orElseThrow(()->notFound("Especialidad no encontrada")));}
 private Scheduling.Specialty specialty(SpecialtyEntity e){return new Scheduling.Specialty(e.id.longValue(),e.code,e.name,e.duration,e.general,e.approvalRequired,e.active);}
 public List<Scheduling.Professional> professionals(long specialtyId,long locationId){return professionals.availableFor(specialtyId,locationId).stream().map(this::professional).toList();}
 private Scheduling.Professional professional(ProfessionalEntity e){String name=users.findById(e.userId).map(u->u.firstName+" "+u.lastName).orElse(e.code); return new Scheduling.Professional(e.id,e.userId,e.code,name,e.active);}
 public boolean canAttend(long p,long l,long s){return locations.findById((short)l).filter(x->x.active).isPresent() && professionals.canAttend(p,l,s)>0;}
 public List<Scheduling.Slot> available(long specialtyId,long locationId,Long professionalId,LocalDate date,int minutes){
  List<ProfessionalEntity> candidates=professionalId==null?professionals.availableFor(specialtyId,locationId):professionals.findById(professionalId).filter(p->canAttend(p.id,locationId,specialtyId)).stream().toList();
  LocalDateTime from=date.atStartOfDay(), to=from.plusDays(1); int required=minutes/30; List<Scheduling.Slot> result=new ArrayList<>();
  for(ProfessionalEntity p:candidates){List<ProfessionalSlotEntity> free=slots.free(p.id,locationId,from,to); for(int i=0;i+required<=free.size();i++){boolean consecutive=true; for(int j=1;j<required;j++) if(!free.get(i+j).startAt.equals(free.get(i).startAt.plusMinutes(30L*j))) consecutive=false; if(consecutive) result.add(new Scheduling.Slot(p.id,p.code,locationId,free.get(i).startAt,free.get(i).startAt.plusMinutes(minutes)));}}
  return result;
 }
 @Transactional
 public Scheduling.Appointment reserve(long patientId,SchedulingService.Reservation c,int duration,String status,String source){
  LocalDateTime end=c.startAt().plusMinutes(duration); List<ProfessionalSlotEntity> locked=slots.lockRange(c.professionalId(),c.locationId(),c.startAt(),end); int expected=duration/30;
  if(locked.size()!=expected || locked.stream().anyMatch(s->s.appointmentId!=null) || !consecutive(locked,c.startAt())) throw conflict("El horario ya no está disponible");
  AppointmentEntity a=new AppointmentEntity(); a.patientId=patientId;a.professionalId=c.professionalId();a.locationId=(short)c.locationId();a.specialtyId=(short)c.specialtyId();a.statusId=status(status).id;a.reason=c.reason();a.startAt=c.startAt();a.endAt=end;a.createdBy=patientId;a.createdAt=clock.instant();
  if("APPROVED".equals(status)){a.approvedBy=null;a.approvedAt=LocalDateTime.now(clock);} a=appointments.saveAndFlush(a); if(a.id==null) throw new IllegalStateException("Appointment id was not generated"); for(ProfessionalSlotEntity s:locked)s.appointmentId=a.id; slots.saveAll(locked); audit(a.id,a.statusId,"SYSTEM".equals(source)?null:patientId,source,c.reason()); return appointment(a,status);
 }
 @Transactional
 public void decide(long adminId,long id,String decision,String reason){ AppointmentEntity a=appointments.findById(id).orElseThrow(()->notFound("Cita no encontrada")); if(a.statusId.shortValue()!=status("REQUESTED").id.shortValue()) throw conflict("La cita no está pendiente"); String next="APPROVE".equals(decision)?"APPROVED":"REJECTED"; a.statusId=status(next).id; a.approvedBy=adminId;a.approvedAt=LocalDateTime.now(clock); appointments.save(a); if("REJECTED".equals(next)){List<ProfessionalSlotEntity> assigned=slots.findByAppointmentId(id); assigned.forEach(s->s.appointmentId=null); slots.saveAll(assigned);} audit(id,a.statusId,adminId,"ADMIN",reason); }
 @Transactional
 public void createBlock(long professionalUserId,SchedulingService.Block c){ ProfessionalEntity p=professionals.findByUserId(professionalUserId).filter(x->x.active).orElseThrow(()->notFound("Profesional no habilitado")); if(!locations.findById((short)c.locationId()).filter(x->x.active).isPresent() || professionals.hasLocation(p.id,c.locationId())<=0) throw invalid("La sede no está asignada al profesional"); if(!blocks.overlapping(p.id,c.date(),c.start(),c.end()).isEmpty()) throw conflict("El bloque se solapa con otro"); AvailabilityBlockEntity b=new AvailabilityBlockEntity();b.professionalId=p.id;b.locationId=(short)c.locationId();b.date=c.date();b.start=c.start();b.end=c.end();b=blocks.saveAndFlush(b);generateSlots(b,c.date(),c.start(),c.end()); }
 public List<Scheduling.AvailabilityBlock> blocksOf(long professionalUserId){ ProfessionalEntity p=professionals.findByUserId(professionalUserId).filter(x->x.active).orElseThrow(()->notFound("Profesional no habilitado")); return blocks.findByProfessionalIdAndActiveTrueOrderByDateAscStartAsc(p.id).stream().map(this::availabilityBlock).toList(); }
 @Transactional
 public void updateBlock(long professionalUserId,long blockId,SchedulingService.Block c){
  ProfessionalEntity p=professionals.findByUserId(professionalUserId).filter(x->x.active).orElseThrow(()->notFound("Profesional no habilitado"));
  AvailabilityBlockEntity b=ownedFutureBlock(p.id,blockId);
  if(!locations.findById((short)c.locationId()).filter(x->x.active).isPresent() || professionals.hasLocation(p.id,c.locationId())<=0) throw invalid("La sede no está asignada al profesional");
  if(slots.existsCommitted(blockId)) throw conflict("El bloque tiene citas comprometidas");
  if(!blocks.overlappingExcluding(p.id,c.date(),c.start(),c.end(),blockId).isEmpty()) throw conflict("El bloque se solapa con otro");
  slots.deleteByBlockId(blockId);
  b.locationId=(short)c.locationId();b.date=c.date();b.start=c.start();b.end=c.end();blocks.save(b);
  generateSlots(b,c.date(),c.start(),c.end());
 }
 @Transactional
 public void deleteBlock(long professionalUserId,long blockId){
  ProfessionalEntity p=professionals.findByUserId(professionalUserId).filter(x->x.active).orElseThrow(()->notFound("Profesional no habilitado"));
  AvailabilityBlockEntity b=ownedFutureBlock(p.id,blockId);
  if(slots.existsCommitted(blockId)) throw conflict("El bloque tiene citas comprometidas");
  slots.deleteByBlockId(blockId);
  blocks.delete(b);
 }
 private AvailabilityBlockEntity ownedFutureBlock(long professionalId,long blockId){
  AvailabilityBlockEntity b=blocks.findById(blockId).filter(x->x.active && x.professionalId==professionalId).orElseThrow(()->notFound("Bloque no encontrado"));
  if(!b.date.isAfter(LocalDate.now(clock))) throw invalid("El bloque ya no es futuro");
  return b;
 }
 private void generateSlots(AvailabilityBlockEntity b,LocalDate date,LocalTime start,LocalTime end){ List<ProfessionalSlotEntity> generated=new ArrayList<>(); for(LocalDateTime t=date.atTime(start);t.isBefore(date.atTime(end));t=t.plusMinutes(30)){ProfessionalSlotEntity s=new ProfessionalSlotEntity();s.blockId=b.id;s.startAt=t;s.endAt=t.plusMinutes(30);generated.add(s);} slots.saveAll(generated); }
 private Scheduling.AvailabilityBlock availabilityBlock(AvailabilityBlockEntity b){return new Scheduling.AvailabilityBlock(b.id,b.professionalId,b.locationId.longValue(),b.date,b.start,b.end);}
 private boolean consecutive(List<ProfessionalSlotEntity> xs,LocalDateTime start){for(int i=0;i<xs.size();i++)if(!xs.get(i).startAt.equals(start.plusMinutes(30L*i)))return false;return true;}
 private AppointmentStatusEntity status(String code){return statuses.findByCode(code).orElseThrow(()->new IllegalStateException("Estado fijo ausente"));}
 private void audit(long appointment,short status,Long actor,String source,String reason){AppointmentHistoryEntity h=new AppointmentHistoryEntity();h.appointmentId=appointment;h.statusId=status;h.actorId=actor;h.source=source;h.reason=reason;h.changedAt=clock.instant();history.save(h);}
 private Scheduling.Appointment appointment(AppointmentEntity a,String code){return new Scheduling.Appointment(a.id,a.patientId,a.professionalId,a.locationId.longValue(),a.specialtyId.longValue(),code,a.startAt,a.endAt,a.reason);}
 private SchedulingFailure conflict(String m){return new SchedulingFailure(SchedulingFailure.Kind.CONFLICT,m);} private SchedulingFailure invalid(String m){return new SchedulingFailure(SchedulingFailure.Kind.INVALID,m);} private SchedulingFailure notFound(String m){return new SchedulingFailure(SchedulingFailure.Kind.NOT_FOUND,m);}

 public List<Scheduling.Specialty> allSpecialties(){return specialties.findAllByOrderByName().stream().map(this::specialty).toList();}
 @Transactional
 public Scheduling.Specialty createSpecialty(String code,String name,int duration,boolean general,boolean approvalRequired){
  if(specialties.findByCode(code).isPresent()) throw conflict("Código de especialidad ya existe");
  SpecialtyEntity e=new SpecialtyEntity(); e.code=code;e.name=name;e.duration=(short)duration;e.general=general;e.approvalRequired=approvalRequired;e.active=true;
  try{ return specialty(specialties.saveAndFlush(e)); } catch(DataIntegrityViolationException ex){ throw conflict("Código o nombre de especialidad ya existe"); }
 }
 @Transactional
 public Scheduling.Specialty updateSpecialty(long id,String code,String name,int duration,boolean general,boolean approvalRequired){
  SpecialtyEntity e=specialties.findById((short)id).orElseThrow(()->notFound("Especialidad no encontrada"));
  e.code=code;e.name=name;e.duration=(short)duration;e.general=general;e.approvalRequired=approvalRequired;
  try{ return specialty(specialties.save(e)); } catch(DataIntegrityViolationException ex){ throw conflict("Código o nombre de especialidad ya existe"); }
 }
 @Transactional
 public void setSpecialtyActive(long id,boolean active){ SpecialtyEntity e=specialties.findById((short)id).orElseThrow(()->notFound("Especialidad no encontrada")); e.active=active; specialties.save(e); }

 public List<Scheduling.ProfessionalAdminView> adminProfessionals(){ return professionals.findAll().stream().map(this::adminView).toList(); }
 @Transactional
 public Scheduling.ProfessionalAdminView createProfessional(long userId,String code,String license){
  if(professionals.findByUserId(userId).isPresent()) throw invalid("El usuario ya tiene perfil profesional");
  ProfessionalEntity p=new ProfessionalEntity(); p.userId=userId;p.code=code;p.license=license;p.active=true;
  try{ p=professionals.saveAndFlush(p); } catch(DataIntegrityViolationException ex){ throw conflict("Código o matrícula ya registrados"); }
  return adminView(p);
 }
 @Transactional
 public void setProfessionalActive(long professionalId,boolean active){ ProfessionalEntity p=professionals.findById(professionalId).orElseThrow(()->notFound("Profesional no encontrado")); p.active=active; professionals.save(p); }
 @Transactional
 public void assignSpecialty(long professionalId,long specialtyId,boolean primary){
  if(!professionals.existsById(professionalId)) throw notFound("Profesional no encontrado");
  if(!specialties.findById((short)specialtyId).filter(x->x.active).isPresent()) throw invalid("Especialidad inexistente o inactiva");
  if(primary){ List<ProfessionalSpecialtyEntity> existing=professionalSpecialties.findByProfessionalId(professionalId); existing.forEach(ps->ps.primary=false); professionalSpecialties.saveAll(existing); }
  ProfessionalSpecialtyEntity assoc=professionalSpecialties.findByProfessionalIdAndSpecialtyId(professionalId,(short)specialtyId)
   .orElseGet(()->{ProfessionalSpecialtyEntity e=new ProfessionalSpecialtyEntity(); e.professionalId=professionalId; e.specialtyId=(short)specialtyId; return e;});
  assoc.active=true; assoc.primary=primary; professionalSpecialties.save(assoc);
 }
 @Transactional
 public void removeSpecialty(long professionalId,long specialtyId){
  professionalSpecialties.findByProfessionalIdAndSpecialtyId(professionalId,(short)specialtyId).ifPresent(e->{e.active=false;e.primary=false;professionalSpecialties.save(e);});
 }
 @Transactional
 public void assignLocation(long professionalId,long locationId){
  if(!professionals.existsById(professionalId)) throw notFound("Profesional no encontrado");
  if(!locations.findById((short)locationId).filter(x->x.active).isPresent()) throw invalid("Sede inexistente o inactiva");
  ProfessionalLocationEntity assoc=professionalLocations.findByProfessionalIdAndLocationId(professionalId,(short)locationId)
   .orElseGet(()->{ProfessionalLocationEntity e=new ProfessionalLocationEntity(); e.professionalId=professionalId; e.locationId=(short)locationId; return e;});
  assoc.active=true; professionalLocations.save(assoc);
 }
 @Transactional
 public void removeLocation(long professionalId,long locationId){
  professionalLocations.findByProfessionalIdAndLocationId(professionalId,(short)locationId).ifPresent(e->{e.active=false;professionalLocations.save(e);});
 }
 private Scheduling.ProfessionalAdminView adminView(ProfessionalEntity p){
  String name=users.findById(p.userId).map(u->u.firstName+" "+u.lastName).orElse(p.code);
  List<Scheduling.ProfessionalSpecialty> specs=professionalSpecialties.findByProfessionalId(p.id).stream().map(ps->new Scheduling.ProfessionalSpecialty(ps.specialtyId,ps.primary,ps.active)).toList();
  List<Long> locs=professionalLocations.findByProfessionalId(p.id).stream().map(pl->pl.locationId.longValue()).toList();
  return new Scheduling.ProfessionalAdminView(p.id,p.userId,p.code,name,p.license,p.active,specs,locs);
 }
}
