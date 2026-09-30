package co.com.fcv.training.citas.application;

import co.com.fcv.training.citas.domain.Account;
import co.com.fcv.training.citas.domain.Identity;
import co.com.fcv.training.citas.domain.Scheduling;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

/** Orchestrates admin-only professional identity + profile creation (HU-015/016/017). */
public class AdminProfessionalService {
    public record CreateProfessional(String firstName, String lastName, String documentType, String documentNumber,
                                     String email, String phone, String password, String code, String license) {}

    private final Ports.Accounts accounts;
    private final Ports.Passwords passwords;
    private final Ports.Transactions transactions;
    private final Ports.SchedulingPort scheduling;

    public AdminProfessionalService(Ports.Accounts accounts, Ports.Passwords passwords, Ports.Transactions transactions, Ports.SchedulingPort scheduling) {
        this.accounts = accounts;
        this.passwords = passwords;
        this.transactions = transactions;
        this.scheduling = scheduling;
    }

    public Scheduling.ProfessionalAdminView create(CreateProfessional input) {
        return transactions.run(() -> {
            String email = Identity.email(input.email());
            String type = Identity.documentType(input.documentType());
            String number = Identity.required(input.documentNumber());
            if (accounts.existsEmail(email) || accounts.existsDocument(type, number)) throw new DuplicateIdentity();
            String password = input.password();
            if (password == null || password.isBlank()) throw new IllegalArgumentException("Contraseña obligatoria");
            if (password.getBytes(StandardCharsets.UTF_8).length > 72) throw new IllegalArgumentException("Contraseña demasiado larga");
            Account account = new Account(null, Identity.required(input.firstName()), Identity.required(input.lastName()),
                    type, number, email, Identity.required(input.phone()), passwords.hash(password), Set.of("PROFESSIONAL"));
            Account saved = accounts.save(account);
            return scheduling.createProfessional(saved.id(), Identity.required(input.code()), Identity.required(input.license()));
        });
    }

    public List<Scheduling.ProfessionalAdminView> all() { return scheduling.adminProfessionals(); }
    public void setActive(long professionalId, boolean active) { scheduling.setProfessionalActive(professionalId, active); }
    public void assignSpecialty(long professionalId, long specialtyId, boolean primary) { scheduling.assignSpecialty(professionalId, specialtyId, primary); }
    public void removeSpecialty(long professionalId, long specialtyId) { scheduling.removeSpecialty(professionalId, specialtyId); }
    public void assignLocation(long professionalId, long locationId) { scheduling.assignLocation(professionalId, locationId); }
    public void removeLocation(long professionalId, long locationId) { scheduling.removeLocation(professionalId, locationId); }
}
