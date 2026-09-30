package co.com.fcv.training.citas.adapter.web;

import co.com.fcv.training.citas.application.AdminProfessionalService;
import co.com.fcv.training.citas.domain.Scheduling;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/professionals")
class AdminProfessionalController {
    record CreateRequest(@NotBlank @Size(max = 120) String firstName, @NotBlank @Size(max = 120) String lastName,
                         @NotBlank @Size(max = 30) String documentType, @NotBlank @Size(max = 80) String documentNumber,
                         @NotBlank @Size(max = 254) String email, @NotBlank @Size(max = 40) String phone,
                         @NotBlank String password, @NotBlank @Size(max = 40) String code, @NotBlank @Size(max = 80) String license) {}
    record SpecialtyAssignmentRequest(@Positive long specialtyId, boolean primary) {}
    record LocationAssignmentRequest(@Positive long locationId) {}
    record SpecialtyAssignmentView(long specialtyId, boolean primary, boolean active) {}
    record ProfessionalView(long id, long userId, String code, String name, String license, boolean active,
                            List<SpecialtyAssignmentView> specialties, List<Long> locationIds) {}

    private final AdminProfessionalService service;
    AdminProfessionalController(AdminProfessionalService service) { this.service = service; }

    @PostMapping @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<ProfessionalView> create(@Valid @RequestBody CreateRequest r) {
        Scheduling.ProfessionalAdminView created = service.create(new AdminProfessionalService.CreateProfessional(
                r.firstName(), r.lastName(), r.documentType(), r.documentNumber(), r.email(), r.phone(), r.password(), r.code(), r.license()));
        return ResponseEntity.status(HttpStatus.CREATED).body(view(created));
    }

    @GetMapping @PreAuthorize("hasRole('ADMIN')")
    List<ProfessionalView> all() { return service.all().stream().map(this::view).toList(); }

    @PostMapping("/{id}/activate") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> activate(@PathVariable long id) { service.setActive(id, true); return ResponseEntity.noContent().build(); }

    @PostMapping("/{id}/deactivate") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> deactivate(@PathVariable long id) { service.setActive(id, false); return ResponseEntity.noContent().build(); }

    @PostMapping("/{id}/specialties") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> assignSpecialty(@PathVariable long id, @Valid @RequestBody SpecialtyAssignmentRequest r) {
        service.assignSpecialty(id, r.specialtyId(), r.primary());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/specialties/{specialtyId}") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> removeSpecialty(@PathVariable long id, @PathVariable long specialtyId) {
        service.removeSpecialty(id, specialtyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/locations") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> assignLocation(@PathVariable long id, @Valid @RequestBody LocationAssignmentRequest r) {
        service.assignLocation(id, r.locationId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/locations/{locationId}") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> removeLocation(@PathVariable long id, @PathVariable long locationId) {
        service.removeLocation(id, locationId);
        return ResponseEntity.noContent().build();
    }

    private ProfessionalView view(Scheduling.ProfessionalAdminView v) {
        return new ProfessionalView(v.id(), v.userId(), v.code(), v.name(), v.license(), v.active(),
                v.specialties().stream().map(s -> new SpecialtyAssignmentView(s.specialtyId(), s.primary(), s.active())).toList(),
                v.locationIds());
    }
}
