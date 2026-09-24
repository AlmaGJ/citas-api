package co.com.fcv.training.citas.application;

import co.com.fcv.training.citas.domain.Scheduling;
import java.time.*;
import java.util.List;

/** Application orchestration. Persistence/locking stays behind the Scheduling port. */
public final class SchedulingService {
    private final Ports.SchedulingPort port; private final Clock clock;
    public SchedulingService(Ports.SchedulingPort port, Clock clock) { this.port = port; this.clock = clock; }
    public List<Scheduling.Location> locations() { return port.locations(); }
    public List<Scheduling.Specialty> specialties() { return port.specialties(); }
    public List<Scheduling.Professional> professionals(long specialtyId, long locationId) { return port.professionals(specialtyId, locationId); }
    public List<Scheduling.Slot> availability(long specialtyId, long locationId, Long professionalId, LocalDate date) {
        Scheduling.Specialty specialty = port.specialty(specialtyId);
        if (!specialty.active()) throw invalid("La especialidad no está activa");
        return port.available(specialtyId, locationId, professionalId, date, specialty.durationMinutes());
    }
    public Scheduling.Appointment reserve(long patientId, Reservation cmd) {
        if (cmd.startAt().getSecond()!=0 || cmd.startAt().getNano()!=0 || cmd.startAt().getMinute()%30 != 0 || !cmd.startAt().isAfter(LocalDateTime.now(clock))) throw invalid("Horario inválido o en el pasado");
        Scheduling.Specialty specialty=port.specialty(cmd.specialtyId());
        if (!specialty.active() || !port.canAttend(cmd.professionalId(), cmd.locationId(), cmd.specialtyId())) throw invalid("Selección no disponible");
        return port.reserve(patientId, cmd, specialty.durationMinutes(), specialty.general() ? "APPROVED" : "REQUESTED", specialty.general() ? "SYSTEM" : "USER");
    }
    public void decide(long adminId, long appointmentId, Decision decision) {
        if (!("APPROVE".equals(decision.decision()) || "REJECT".equals(decision.decision()))) throw invalid("Decisión inválida");
        if ("REJECT".equals(decision.decision()) && (decision.reason()==null || decision.reason().isBlank())) throw invalid("El rechazo requiere motivo");
        port.decide(adminId, appointmentId, decision.decision(), decision.reason());
    }
    public void createBlock(long professionalUserId, Block cmd) {
        if (!cmd.date().isAfter(LocalDate.now(clock)) || !cmd.end().isAfter(cmd.start()) || cmd.start().getMinute()%30 != 0 || cmd.end().getMinute()%30 != 0) throw invalid("Bloque inválido o en el pasado");
        port.createBlock(professionalUserId,cmd);
    }
    public record Reservation(long professionalId,long locationId,long specialtyId,LocalDateTime startAt,String reason) {}
    public record Decision(String decision,String reason) {}
    public record Block(long locationId,LocalDate date,LocalTime start,LocalTime end) {}
    private SchedulingFailure invalid(String value) { return new SchedulingFailure(SchedulingFailure.Kind.INVALID,value); }
}
