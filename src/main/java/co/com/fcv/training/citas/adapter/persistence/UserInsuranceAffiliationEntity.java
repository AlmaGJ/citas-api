package co.com.fcv.training.citas.adapter.persistence;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Instant;

@Entity
@Table(name = "user_insurance_affiliations")
class UserInsuranceAffiliationEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column(name = "user_id", nullable = false) Long userId;
    @Column(name = "plan_id", nullable = false) Long planId;
    @Column(name = "membership_number", nullable = false, length = 80) String membershipNumber;
    @Column(name = "is_current", nullable = false) boolean current;
    @Column(name = "valid_from") LocalDate validFrom;
    @Column(name = "valid_to") LocalDate validTo;
    @Column(name = "created_at", nullable = false) Instant createdAt;
    protected UserInsuranceAffiliationEntity() {}
    UserInsuranceAffiliationEntity(Long userId, Long planId) {
        this.userId = userId; this.planId = planId;
        this.membershipNumber = "FCV-" + userId + "-" + planId;
        this.current = true; this.validFrom = LocalDate.now(); this.createdAt = Instant.now();
    }
}
