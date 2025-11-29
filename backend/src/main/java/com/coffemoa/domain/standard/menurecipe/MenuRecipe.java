package com.coffemoa.domain.standard.menurecipe;

import static jakarta.persistence.FetchType.LAZY;

import com.coffemoa.domain.standard.cafemenuprice.CafeMenuPrice;
import com.coffemoa.domain.standard.material.Material;
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
    name = "menu_recipe",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_menu_recipe",
        columnNames = {"cafe_menu_price_id", "material_id", "deleted_at"}
    )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRecipe extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * 어떤 메뉴 옵션(HOT/Large 등)의 레시피인지
   */
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "cafe_menu_price_id", nullable = false)
  private CafeMenuPrice cafeMenuPrice;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "material_id", nullable = false)
  private Material material;

  @Column(name = "usage_amount", nullable = false)
  private BigDecimal usageAmount;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "unit_id", nullable = false)
  private Unit unit;
}
