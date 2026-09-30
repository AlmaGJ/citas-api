package co.com.fcv.training.citas.adapter.persistence;

import co.com.fcv.training.citas.application.InsuranceFailure;
import co.com.fcv.training.citas.application.Ports;
import co.com.fcv.training.citas.domain.InsuranceCatalog;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import jakarta.persistence.EntityManager;
import java.util.List;

@Component
class InsuranceJpaAdapter implements Ports.Insurance {
    private final InsurancePlansJpa plans;
    private final UserAffiliationsJpa affiliations;
    private final EpsJpa eps;
    private final EntityManager entityManager;
    InsuranceJpaAdapter(InsurancePlansJpa plans, UserAffiliationsJpa affiliations, EpsJpa eps, EntityManager entityManager) {
        this.plans = plans; this.affiliations = affiliations; this.eps = eps; this.entityManager = entityManager;
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

    public List<InsuranceCatalog.Eps> allEps() { return eps.findAll().stream().map(this::eps).toList(); }

    public InsuranceCatalog.Eps createEps(String code, String name) {
        if (eps.findByCode(code).isPresent()) throw new InsuranceFailure(InsuranceFailure.Kind.CONFLICT, "Código de EPS ya existe");
        EpsEntity e = new EpsEntity(); e.code = code; e.name = name; e.active = true;
        return eps(eps.saveAndFlush(e));
    }

    public InsuranceCatalog.Eps updateEps(long id, String code, String name) {
        EpsEntity e = eps.findById(id).orElseThrow(() -> new InsuranceFailure(InsuranceFailure.Kind.NOT_FOUND, "EPS no encontrada"));
        e.code = code; e.name = name;
        return eps(eps.save(e));
    }

    public void setEpsActive(long id, boolean active) {
        EpsEntity e = eps.findById(id).orElseThrow(() -> new InsuranceFailure(InsuranceFailure.Kind.NOT_FOUND, "EPS no encontrada"));
        e.active = active; eps.save(e);
    }

    public List<InsuranceCatalog.Plan> plansOf(long epsId) { return plans.findByEpsId(epsId).stream().map(this::plan).toList(); }

    public InsuranceCatalog.Plan createPlan(long epsId, long regimeId, String code, String name) {
        if (!eps.existsById(epsId)) throw new InsuranceFailure(InsuranceFailure.Kind.INVALID, "EPS inexistente");
        InsurancePlanEntity p = new InsurancePlanEntity();
        p.epsId = epsId; p.regimeId = (short) regimeId; p.code = code; p.name = name; p.active = true;
        try { return plan(plans.saveAndFlush(p)); }
        catch (DataIntegrityViolationException ex) { throw new InsuranceFailure(InsuranceFailure.Kind.CONFLICT, "Código de plan ya existe para esa EPS"); }
    }

    public InsuranceCatalog.Plan updatePlan(long id, String code, String name, long regimeId) {
        InsurancePlanEntity p = plans.findById(id).orElseThrow(() -> new InsuranceFailure(InsuranceFailure.Kind.NOT_FOUND, "Plan no encontrado"));
        p.code = code; p.name = name; p.regimeId = (short) regimeId;
        try { return plan(plans.save(p)); }
        catch (DataIntegrityViolationException ex) { throw new InsuranceFailure(InsuranceFailure.Kind.CONFLICT, "Código de plan ya existe para esa EPS"); }
    }

    public void setPlanActive(long id, boolean active) {
        InsurancePlanEntity p = plans.findById(id).orElseThrow(() -> new InsuranceFailure(InsuranceFailure.Kind.NOT_FOUND, "Plan no encontrado"));
        p.active = active; plans.save(p);
    }

    public List<InsuranceCatalog.Regime> allRegimes() {
        return entityManager.createNativeQuery("select id, code, name from insurance_regimes order by name")
                .getResultList().stream().map(row -> {
            Object[] v = (Object[]) row;
            return new InsuranceCatalog.Regime(((Number) v[0]).longValue(), (String) v[1], (String) v[2]);
        }).toList();
    }

    private InsuranceCatalog.Eps eps(EpsEntity e) { return new InsuranceCatalog.Eps(e.id, e.code, e.name, e.active); }
    private InsuranceCatalog.Plan plan(InsurancePlanEntity p) { return new InsuranceCatalog.Plan(p.id, p.epsId, p.regimeId, p.code, p.name, p.active); }
}
