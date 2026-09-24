package co.com.fcv.training.citas.adapter.persistence;

import co.com.fcv.training.citas.application.Ports;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import java.util.List;

@Component
class InsuranceJpaAdapter implements Ports.Insurance {
    private final InsurancePlansJpa plans;
    private final UserAffiliationsJpa affiliations;
    private final EntityManager entityManager;
    InsuranceJpaAdapter(InsurancePlansJpa plans, UserAffiliationsJpa affiliations, EntityManager entityManager) {
        this.plans = plans; this.affiliations = affiliations; this.entityManager = entityManager;
    }
    public boolean isActivePlan(Long planId) { return plans.existsByIdAndActiveTrue(planId); }
    public void createCurrentAffiliation(Long userId, Long planId) {
        affiliations.save(new UserInsuranceAffiliationEntity(userId, planId));
    }
    public List<Ports.ActivePlan> activePlans() {
        return entityManager.createNativeQuery("""
                select p.id, p.name, e.name, r.name from eps_plans p
                join eps e on e.id=p.eps_id join insurance_regimes r on r.id=p.regime_id
                where p.active=true and e.active=true order by e.name, p.name
                """).getResultList().stream().map(row -> {
            Object[] v = (Object[]) row;
            return new Ports.ActivePlan(((Number) v[0]).longValue(), (String) v[1], (String) v[2], (String) v[3]);
        }).toList();
    }
}
