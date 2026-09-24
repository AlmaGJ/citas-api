package co.com.fcv.training.citas.adapter.persistence;

import jakarta.persistence.*;

@Entity
@Table(name = "eps_plans")
class InsurancePlanEntity {
    @Id Long id;
    @Column(nullable = false) boolean active;
}
