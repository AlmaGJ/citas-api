package co.com.fcv.training.citas.application;

import co.com.fcv.training.citas.domain.Scheduling;
import org.junit.jupiter.api.Test;
import java.time.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class SchedulingServiceTest {
 private final Clock clock=Clock.fixed(Instant.parse("2026-09-24T12:00:00Z"),ZoneId.of("America/Bogota"));
 @Test void generalAppointmentIsApprovedAndSpecializedIsRequested(){
  Fake port=new Fake(); SchedulingService service=new SchedulingService(port,clock); LocalDateTime start=LocalDateTime.of(2026,9,25,9,0);
  service.reserve(9,new SchedulingService.Reservation(7,1,1,start,null)); assertEquals("APPROVED",port.status);
  port.specialty=new Scheduling.Specialty(2,"CARD","Card",60,false,true,true); service.reserve(9,new SchedulingService.Reservation(7,1,2,start,null)); assertEquals("REQUESTED",port.status); assertEquals(60,port.duration);
 }
 @Test void specializedRejectionNeedsReason(){
  SchedulingService service=new SchedulingService(new Fake(),clock);
  assertThrows(SchedulingFailure.class,()->service.decide(1,2,new SchedulingService.Decision("REJECT","  ")));
 }
 @Test void onlyThirtyMinuteBoundariesAreAccepted(){
  SchedulingService service=new SchedulingService(new Fake(),clock);
  assertThrows(SchedulingFailure.class,()->service.reserve(9,new SchedulingService.Reservation(7,1,1,LocalDateTime.of(2026,9,25,9,15),null)));
 }
 @Test void blockMustBeInTheFuture(){
  SchedulingService service=new SchedulingService(new Fake(),clock);
  assertThrows(SchedulingFailure.class,()->service.createBlock(7,new SchedulingService.Block(1,LocalDate.of(2026,9,24),LocalTime.of(9,0),LocalTime.of(10,0))));
  assertThrows(SchedulingFailure.class,()->service.updateBlock(7,1,new SchedulingService.Block(1,LocalDate.of(2026,9,24),LocalTime.of(9,0),LocalTime.of(10,0))));
 }
 @Test void blockEndMustBeAfterStartOnThirtyMinuteBoundaries(){
  SchedulingService service=new SchedulingService(new Fake(),clock);
  assertThrows(SchedulingFailure.class,()->service.updateBlock(7,1,new SchedulingService.Block(1,LocalDate.of(2026,9,25),LocalTime.of(10,0),LocalTime.of(9,0))));
  assertThrows(SchedulingFailure.class,()->service.updateBlock(7,1,new SchedulingService.Block(1,LocalDate.of(2026,9,25),LocalTime.of(9,15),LocalTime.of(10,0))));
 }
 @Test void validUpdateAndDeleteDelegateToPort(){
  Fake port=new Fake(); SchedulingService service=new SchedulingService(port,clock);
  service.updateBlock(7,1,new SchedulingService.Block(1,LocalDate.of(2026,9,25),LocalTime.of(9,0),LocalTime.of(10,0)));
  assertEquals(1L,port.updatedBlockId);
  service.deleteBlock(7,1);
  assertEquals(1L,port.deletedBlockId);
 }
 @Test void specialtyDurationMustBeThirtyOrSixtyMinutes(){
  SchedulingService service=new SchedulingService(new Fake(),clock);
  assertThrows(SchedulingFailure.class,()->service.createSpecialty("DERM","Dermatología",45,false,true));
  assertThrows(SchedulingFailure.class,()->service.updateSpecialty(1,"DERM","Dermatología",45,false,true));
 }
 @Test void validSpecialtyCreationDelegatesToPort(){
  Fake port=new Fake(); SchedulingService service=new SchedulingService(port,clock);
  service.createSpecialty("DERM","Dermatología",30,false,true);
  assertEquals("DERM",port.createdSpecialtyCode);
  service.setSpecialtyActive(1,false);
  assertEquals(false,port.specialtyActiveSet);
 }
 static class Fake implements Ports.SchedulingPort {
  Scheduling.Specialty specialty=new Scheduling.Specialty(1,"GEN","General",30,true,false,true); String status; int duration;
  Long updatedBlockId; Long deletedBlockId; String createdSpecialtyCode; Boolean specialtyActiveSet;
  public List<Scheduling.Location> locations(){return List.of();} public List<Scheduling.Specialty> specialties(){return List.of(specialty);} public Scheduling.Specialty specialty(long id){return specialty;} public List<Scheduling.Professional> professionals(long s,long l){return List.of();} public boolean canAttend(long p,long l,long s){return true;} public List<Scheduling.Slot> available(long s,long l,Long p,LocalDate d,int m){return List.of();}
  public Scheduling.Appointment reserve(long patient,SchedulingService.Reservation c,int duration,String status,String source){this.status=status;this.duration=duration;return new Scheduling.Appointment(1,patient,c.professionalId(),c.locationId(),c.specialtyId(),status,c.startAt(),c.startAt().plusMinutes(duration),c.reason());}
  public void decide(long a,long id,String d,String r){} public void createBlock(long p,SchedulingService.Block b){}
  public List<Scheduling.AvailabilityBlock> blocksOf(long professionalUserId){return List.of();}
  public void updateBlock(long professionalUserId,long blockId,SchedulingService.Block b){this.updatedBlockId=blockId;}
  public void deleteBlock(long professionalUserId,long blockId){this.deletedBlockId=blockId;}
  public List<Scheduling.Specialty> allSpecialties(){return List.of(specialty);}
  public Scheduling.Specialty createSpecialty(String code,String name,int duration,boolean general,boolean approvalRequired){this.createdSpecialtyCode=code;return specialty;}
  public Scheduling.Specialty updateSpecialty(long id,String code,String name,int duration,boolean general,boolean approvalRequired){return specialty;}
  public void setSpecialtyActive(long id,boolean active){this.specialtyActiveSet=active;}
  public List<Scheduling.ProfessionalAdminView> adminProfessionals(){return List.of();}
  public Scheduling.ProfessionalAdminView createProfessional(long userId,String code,String license){return new Scheduling.ProfessionalAdminView(1,userId,code,code,license,true,List.of(),List.of());}
  public void setProfessionalActive(long professionalId,boolean active){}
  public void assignSpecialty(long professionalId,long specialtyId,boolean primary){}
  public void removeSpecialty(long professionalId,long specialtyId){}
  public void assignLocation(long professionalId,long locationId){}
  public void removeLocation(long professionalId,long locationId){}
 }
}
