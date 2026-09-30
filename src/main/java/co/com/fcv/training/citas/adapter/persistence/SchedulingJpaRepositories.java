package co.com.fcv.training.citas.adapter.persistence;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.*;
import java.util.*;

interface LocationsJpa extends JpaRepository<LocationEntity,Short> { List<LocationEntity> findByActiveTrueOrderByName(); }
interface SpecialtiesJpa extends JpaRepository<SpecialtyEntity,Short> { List<SpecialtyEntity> findByActiveTrueOrderByName(); Optional<SpecialtyEntity> findByCode(String code); List<SpecialtyEntity> findAllByOrderByName(); }
interface ProfessionalsJpa extends JpaRepository<ProfessionalEntity,Long> {
 Optional<ProfessionalEntity> findByUserId(Long userId);
 @Query(value="select count(*) from professionals p join professional_specialties ps on ps.professional_id=p.id and ps.specialty_id=:specialtyId and ps.active=true join professional_locations pl on pl.professional_id=p.id and pl.location_id=:locationId and pl.active=true where p.id=:professionalId and p.active=true",nativeQuery=true) Integer canAttend(@Param("professionalId") long p,@Param("locationId") long l,@Param("specialtyId") long s);
 @Query(value="select count(*) from professionals p join professional_locations pl on pl.professional_id=p.id and pl.location_id=:locationId and pl.active=true where p.id=:professionalId and p.active=true",nativeQuery=true) Integer hasLocation(@Param("professionalId") long p,@Param("locationId") long l);
 @Query(value="select p.* from professionals p join professional_specialties ps on ps.professional_id=p.id and ps.specialty_id=:specialtyId and ps.active=true join professional_locations pl on pl.professional_id=p.id and pl.location_id=:locationId and pl.active=true where p.active=true order by p.professional_code",nativeQuery=true) List<ProfessionalEntity> availableFor(@Param("specialtyId")long s,@Param("locationId")long l);
}
interface BlocksJpa extends JpaRepository<AvailabilityBlockEntity,Long> {
 @Query("select b from AvailabilityBlockEntity b where b.professionalId=:professionalId and b.date=:date and b.active=true and b.start < :end and b.end > :start") List<AvailabilityBlockEntity> overlapping(@Param("professionalId") long p,@Param("date") LocalDate d,@Param("start") LocalTime s,@Param("end") LocalTime e);
 @Query("select b from AvailabilityBlockEntity b where b.professionalId=:professionalId and b.date=:date and b.active=true and b.id<>:excludeId and b.start < :end and b.end > :start") List<AvailabilityBlockEntity> overlappingExcluding(@Param("professionalId") long p,@Param("date") LocalDate d,@Param("start") LocalTime s,@Param("end") LocalTime e,@Param("excludeId") long excludeId);
 List<AvailabilityBlockEntity> findByProfessionalIdAndActiveTrueOrderByDateAscStartAsc(long professionalId);
}
interface SlotsJpa extends JpaRepository<ProfessionalSlotEntity,Long> {
 @Query(value="select ps.* from professional_slots ps join availability_blocks ab on ab.id=ps.availability_block_id where ab.professional_id=:professionalId and ab.location_id=:locationId and ab.active=true and ps.start_at>=:from and ps.start_at<:to and ps.appointment_id is null order by ps.start_at", nativeQuery=true) List<ProfessionalSlotEntity> free(@Param("professionalId") long p,@Param("locationId") long l,@Param("from") LocalDateTime f,@Param("to") LocalDateTime t);
 @Query(value="select ps.* from professional_slots ps join availability_blocks ab on ab.id=ps.availability_block_id where ab.professional_id=:professionalId and ab.location_id=:locationId and ps.start_at>=:from and ps.start_at<:to order by ps.start_at for update", nativeQuery=true) List<ProfessionalSlotEntity> lockRange(@Param("professionalId") long p,@Param("locationId") long l,@Param("from") LocalDateTime f,@Param("to") LocalDateTime t);
 List<ProfessionalSlotEntity> findByAppointmentId(Long appointmentId);
 @Query("select count(s) > 0 from ProfessionalSlotEntity s where s.blockId=:blockId and s.appointmentId is not null") boolean existsCommitted(@Param("blockId") long blockId);
 @Modifying(clearAutomatically = true, flushAutomatically = true)
 @Query("delete from ProfessionalSlotEntity s where s.blockId=:blockId") void deleteByBlockId(@Param("blockId") Long blockId);
}
interface AppointmentsJpa extends JpaRepository<AppointmentEntity,Long> {}
interface AppointmentStatusesJpa extends JpaRepository<AppointmentStatusEntity,Short> { Optional<AppointmentStatusEntity> findByCode(String code); }
interface AppointmentHistoryJpa extends JpaRepository<AppointmentHistoryEntity,Long> {}
interface ProfessionalSpecialtiesJpa extends JpaRepository<ProfessionalSpecialtyEntity,ProfessionalSpecialtyId> {
 List<ProfessionalSpecialtyEntity> findByProfessionalId(long professionalId);
 Optional<ProfessionalSpecialtyEntity> findByProfessionalIdAndSpecialtyId(long professionalId, short specialtyId);
}
interface ProfessionalLocationsJpa extends JpaRepository<ProfessionalLocationEntity,ProfessionalLocationId> {
 List<ProfessionalLocationEntity> findByProfessionalId(long professionalId);
 Optional<ProfessionalLocationEntity> findByProfessionalIdAndLocationId(long professionalId, short locationId);
}
