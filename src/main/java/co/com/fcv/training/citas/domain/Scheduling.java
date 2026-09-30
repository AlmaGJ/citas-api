package co.com.fcv.training.citas.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/** Framework-free vocabulary for the scheduling bounded context. */
public final class Scheduling {
    private Scheduling() {}
    public record Specialty(long id, String code, String name, int durationMinutes, boolean general, boolean approvalRequired, boolean active) {}
    public record Location(long id, String code, String name, boolean active) {}
    public record Professional(long id, long userId, String code, String name, boolean active) {}
    public record Slot(long professionalId, String professionalName, long locationId, LocalDateTime startAt, LocalDateTime endAt) {}
    public record Appointment(long id, long patientUserId, long professionalId, long locationId, long specialtyId,
                              String status, LocalDateTime startAt, LocalDateTime endAt, String reason) {}
    public record AvailabilityBlock(long id, long professionalId, long locationId, LocalDate date, LocalTime start, LocalTime end) {}
    public record ProfessionalSpecialty(long specialtyId, boolean primary, boolean active) {}
    public record ProfessionalAdminView(long id, long userId, String code, String name, String license, boolean active,
                                        List<ProfessionalSpecialty> specialties, List<Long> locationIds) {}
}
