package co.com.fcv.training.citas.adapter.web;

import co.com.fcv.training.citas.application.Ports;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/catalogs")
class CatalogController {
    private final Ports.Insurance insurance;
    CatalogController(Ports.Insurance insurance) { this.insurance = insurance; }
    @GetMapping("/active-plans")
    List<Ports.ActivePlan> activePlans() {
        return insurance.activePlans();
    }
}
