package co.com.fcv.training.citas.adapter.web;

import co.com.fcv.training.citas.application.Ports;
import co.com.fcv.training.citas.domain.InsuranceCatalog;
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
@RequestMapping("/api/v1/admin")
class AdminInsuranceController {
    record EpsRequest(@NotBlank @Size(max = 30) String code, @NotBlank @Size(max = 150) String name) {}
    record EpsResponse(long id, String code, String name, boolean active) {}
    record PlanRequest(@Positive long regimeId, @NotBlank @Size(max = 50) String code, @NotBlank @Size(max = 150) String name) {}
    record PlanResponse(long id, long epsId, long regimeId, String code, String name, boolean active) {}
    record RegimeResponse(long id, String code, String name) {}

    private final Ports.Insurance insurance;
    AdminInsuranceController(Ports.Insurance insurance) { this.insurance = insurance; }

    @GetMapping("/insurance-regimes") @PreAuthorize("hasRole('ADMIN')")
    List<RegimeResponse> regimes() { return insurance.allRegimes().stream().map(r -> new RegimeResponse(r.id(), r.code(), r.name())).toList(); }

    @GetMapping("/eps") @PreAuthorize("hasRole('ADMIN')")
    List<EpsResponse> allEps() { return insurance.allEps().stream().map(this::response).toList(); }

    @PostMapping("/eps") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<EpsResponse> createEps(@Valid @RequestBody EpsRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(response(insurance.createEps(r.code(), r.name())));
    }

    @PatchMapping("/eps/{id}") @PreAuthorize("hasRole('ADMIN')")
    EpsResponse updateEps(@PathVariable long id, @Valid @RequestBody EpsRequest r) {
        return response(insurance.updateEps(id, r.code(), r.name()));
    }

    @PostMapping("/eps/{id}/activate") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> activateEps(@PathVariable long id) { insurance.setEpsActive(id, true); return ResponseEntity.noContent().build(); }

    @PostMapping("/eps/{id}/deactivate") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> deactivateEps(@PathVariable long id) { insurance.setEpsActive(id, false); return ResponseEntity.noContent().build(); }

    @GetMapping("/eps/{epsId}/plans") @PreAuthorize("hasRole('ADMIN')")
    List<PlanResponse> plans(@PathVariable long epsId) { return insurance.plansOf(epsId).stream().map(this::response).toList(); }

    @PostMapping("/eps/{epsId}/plans") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<PlanResponse> createPlan(@PathVariable long epsId, @Valid @RequestBody PlanRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(response(insurance.createPlan(epsId, r.regimeId(), r.code(), r.name())));
    }

    @PatchMapping("/eps/{epsId}/plans/{id}") @PreAuthorize("hasRole('ADMIN')")
    PlanResponse updatePlan(@PathVariable long epsId, @PathVariable long id, @Valid @RequestBody PlanRequest r) {
        return response(insurance.updatePlan(id, r.code(), r.name(), r.regimeId()));
    }

    @PostMapping("/eps/{epsId}/plans/{id}/activate") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> activatePlan(@PathVariable long epsId, @PathVariable long id) { insurance.setPlanActive(id, true); return ResponseEntity.noContent().build(); }

    @PostMapping("/eps/{epsId}/plans/{id}/deactivate") @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> deactivatePlan(@PathVariable long epsId, @PathVariable long id) { insurance.setPlanActive(id, false); return ResponseEntity.noContent().build(); }

    private EpsResponse response(InsuranceCatalog.Eps e) { return new EpsResponse(e.id(), e.code(), e.name(), e.active()); }
    private PlanResponse response(InsuranceCatalog.Plan p) { return new PlanResponse(p.id(), p.epsId(), p.regimeId(), p.code(), p.name(), p.active()); }
}
