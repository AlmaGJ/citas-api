package co.com.fcv.training.citas.domain;

import java.time.LocalDateTime;

/** Framework-free vocabulary for the scheduling bounded context. */
public final class Scheduling {
    private Scheduling() {}
    public record Specialty(long id, String code, String name, int durationMinutes, boolean general, boolean approvalRequired, boolean active) {}
    public record Location(long id, String code, String name, boolean active) {}
    public record Professional(long id, long userId, String code, String name, boolean active) {}
    public record Slot(long professionalId, long locationId, LocalDateTime startAt, LocalDateTime endAt) {}
    public record Appointment(long id, long patientUserId, long professionalId, long locationId, long specialtyId,
                              String status, LocalDateTime startAt, LocalDateTime endAt, String reason) {}
}
