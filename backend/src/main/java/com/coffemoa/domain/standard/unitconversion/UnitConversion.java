package com.coffemoa.domain.standard.unitconversion;

import static jakarta.persistence.FetchType.LAZY;

import com.coffemoa.domain.standard.unit.Unit;
import com.coffemoa.global.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "unit_conversion",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_unit_conversion",
        columnNames = {"base_unit_id", "target_unit_id", "deleted_at"}
    )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitConversion extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "base_unit_id", nullable = false)
  private Unit baseUnit;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "target_unit_id", nullable = false)
  private Unit targetUnit;

  @Column(name = "ratio", nullable = false)
  private BigDecimal ratio;
}
