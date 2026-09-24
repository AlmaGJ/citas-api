package co.com.fcv.training.citas.application;

import co.com.fcv.training.citas.domain.Account;
import co.com.fcv.training.citas.domain.RefreshSession;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import co.com.fcv.training.citas.domain.Scheduling;
import java.time.LocalDate;
import java.util.List;

public final class Ports {
    private Ports() {}

    public interface Accounts {
        boolean existsEmail(String email);
        boolean existsDocument(String type, String number);
        Optional<Account> byEmail(String email);
        Optional<Account> byId(Long id);
        Account save(Account account);
    }

    public interface Sessions {
        void save(RefreshSession session);
        Optional<RefreshSession> lockByJtiHash(String hash);
        void revoke(Long id, Instant when);
    }

    public interface Passwords {
        String hash(String raw);
        boolean matches(String raw, String hash);
    }

    public record IssuedRefresh(String value, String jti, Instant expiresAt) {}
    public record RefreshIdentity(Long userId, String jti) {}

    public interface Tokens {
        String access(Long userId, Set<String> roles);
        IssuedRefresh refresh(Long userId);
        RefreshIdentity readRefresh(String token);
        long accessSeconds();
    }

    public interface Transactions {
        <T> T run(Supplier<T> work);
    }

    public interface Insurance {
        boolean isActivePlan(Long planId);
        void createCurrentAffiliation(Long userId, Long planId);
        java.util.List<ActivePlan> activePlans();
    }
    public record ActivePlan(Long id, String name, String epsName, String regime) {}

    public interface SchedulingPort {
        List<Scheduling.Location> locations();
        List<Scheduling.Specialty> specialties();
        Scheduling.Specialty specialty(long id);
        List<Scheduling.Professional> professionals(long specialtyId, long locationId);
        boolean canAttend(long professionalId, long locationId, long specialtyId);
        List<Scheduling.Slot> available(long specialtyId, long locationId, Long professionalId, LocalDate date, int durationMinutes);
        Scheduling.Appointment reserve(long patientId, SchedulingService.Reservation command, int durationMinutes, String status, String source);
        void decide(long adminId, long appointmentId, String decision, String reason);
        void createBlock(long professionalUserId, SchedulingService.Block command);
    }
}
