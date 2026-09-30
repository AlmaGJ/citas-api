package co.com.fcv.training.citas.adapter.web;

import co.com.fcv.training.citas.application.SchedulingService;
import co.com.fcv.training.citas.domain.Scheduling;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/specialties")
class AdminSpecialtyController {
    record SpecialtyRequest(@NotBlank @Size(max = 50) String code, @NotBlank @Size(max = 150) String name,
                            @Min(30) @Max(60) int durationMinutes, boolean general, boolean approvalRequired) {}
    record SpecialtyResponse(long id, String code, String name, int durationMinutes, boolean general, boolean approvalRequired, boolean active) {}

    private final SchedulingService scheduling;
    AdminSpecialtyController(SchedulingService scheduling) { this.scheduling = scheduling; }

    @GetMapping @PreAuthorize("hasRole('ADMIN')")
    List<SpecialtyResponse> all() { return scheduling.allSpecialties().stream().map(this::response).toList(); }

    @PostMapping @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<SpecialtyResponse> create(@Valid @RequestBody SpecialtyRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(response(scheduling.createSpecialty(r.code(), r.name(), r.durationMinutes(), r.general(), r.approvalRequired())));
    }

    @PatchMapping("/{id}") @PreAuthorize("hasRole('ADMIN')")
    SpecialtyResponse update(@PathVariable long id, @Valid @RequestBody SpecialtyRequest r) {
        return response(scheduling.updateSpecialty(id, r.code(), r.name(), r.durationMinutes(), r.general(), r.approvalRequired()));
    }

    @PostMapping("/{id}/activate") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> activate(@PathVariable long id) { scheduling.setSpecialtyActive(id, true); return ResponseEntity.noContent().build(); }

    @PostMapping("/{id}/deactivate") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> deactivate(@PathVariable long id) { scheduling.setSpecialtyActive(id, false); return ResponseEntity.noContent().build(); }

    private SpecialtyResponse response(Scheduling.Specialty s) { return new SpecialtyResponse(s.id(), s.code(), s.name(), s.durationMinutes(), s.general(), s.approvalRequired(), s.active()); }
}
