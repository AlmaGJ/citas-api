package co.com.fcv.training.citas.application;

import co.com.fcv.training.citas.domain.Account;
import co.com.fcv.training.citas.domain.RefreshSession;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import co.com.fcv.training.citas.domain.Scheduling;
import co.com.fcv.training.citas.domain.InsuranceCatalog;
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
        List<InsuranceCatalog.Eps> allEps();
        InsuranceCatalog.Eps createEps(String code, String name);
        InsuranceCatalog.Eps updateEps(long id, String code, String name);
        void setEpsActive(long id, boolean active);
        List<InsuranceCatalog.Plan> plansOf(long epsId);
        InsuranceCatalog.Plan createPlan(long epsId, long regimeId, String code, String name);
        InsuranceCatalog.Plan updatePlan(long id, String code, String name, long regimeId);
        void setPlanActive(long id, boolean active);
        List<InsuranceCatalog.Regime> allRegimes();
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
        List<Scheduling.AvailabilityBlock> blocksOf(long professionalUserId);
        void updateBlock(long professionalUserId, long blockId, SchedulingService.Block command);
        void deleteBlock(long professionalUserId, long blockId);
        List<Scheduling.Specialty> allSpecialties();
        Scheduling.Specialty createSpecialty(String code, String name, int durationMinutes, boolean general, boolean approvalRequired);
        Scheduling.Specialty updateSpecialty(long id, String code, String name, int durationMinutes, boolean general, boolean approvalRequired);
        void setSpecialtyActive(long id, boolean active);
        List<Scheduling.ProfessionalAdminView> adminProfessionals();
        Scheduling.ProfessionalAdminView createProfessional(long userId, String code, String license);
        void setProfessionalActive(long professionalId, boolean active);
        void assignSpecialty(long professionalId, long specialtyId, boolean primary);
        void removeSpecialty(long professionalId, long specialtyId);
        void assignLocation(long professionalId, long locationId);
        void removeLocation(long professionalId, long locationId);
    }
}
