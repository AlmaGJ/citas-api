package co.com.fcv.training.citas.adapter.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "eps_plans")
class InsurancePlanEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @Column(name = "eps_id", nullable = false) Long epsId;
    @Column(name = "regime_id", nullable = false) Short regimeId;
    @Column(nullable = false, length = 50) String code;
    @Column(nullable = false, length = 150) String name;
    @Column(nullable = false) boolean active;
    protected InsurancePlanEntity() {}
}
