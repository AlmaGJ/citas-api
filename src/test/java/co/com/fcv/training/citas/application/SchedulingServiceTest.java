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
 static class Fake implements Ports.SchedulingPort {
  Scheduling.Specialty specialty=new Scheduling.Specialty(1,"GEN","General",30,true,false,true); String status; int duration;
  public List<Scheduling.Location> locations(){return List.of();} public List<Scheduling.Specialty> specialties(){return List.of(specialty);} public Scheduling.Specialty specialty(long id){return specialty;} public List<Scheduling.Professional> professionals(long s,long l){return List.of();} public boolean canAttend(long p,long l,long s){return true;} public List<Scheduling.Slot> available(long s,long l,Long p,LocalDate d,int m){return List.of();}
  public Scheduling.Appointment reserve(long patient,SchedulingService.Reservation c,int duration,String status,String source){this.status=status;this.duration=duration;return new Scheduling.Appointment(1,patient,c.professionalId(),c.locationId(),c.specialtyId(),status,c.startAt(),c.startAt().plusMinutes(duration),c.reason());}
  public void decide(long a,long id,String d,String r){} public void createBlock(long p,SchedulingService.Block b){}
 }
}
