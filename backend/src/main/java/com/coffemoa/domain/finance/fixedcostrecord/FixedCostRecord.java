package com.coffemoa.domain.finance.fixedcostrecord;

import com.coffemoa.domain.finance.fixedcost.FixedCost;
import com.coffemoa.global.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fixed_cost_record")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedCostRecord extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fixed_cost_id", nullable = false)
  private FixedCost fixedCost;

  /**
   * 기간 값 YEARLY  → "2025" MONTHLY → "2025-11" DAILY   → "2025-11-11"
   */
  @Column(nullable = false, length = 20)
  private String periodValue;

  @Column(nullable = false)
  private Double amount;
}
