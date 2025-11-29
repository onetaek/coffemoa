package com.coffemoa.domain.standard.menurecipe;

import com.coffemoa.domain.standard.cafemenuprice.CafeMenuPriceResponse;
import com.coffemoa.domain.standard.material.MaterialResponse;
import com.coffemoa.domain.standard.unit.UnitResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRecipeResponse {

  private Long id;
  private CafeMenuPriceResponse cafeMenuPrice;
  private MaterialResponse material;
  private BigDecimal usageAmount;
  private UnitResponse unit;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static MenuRecipeResponse fromEntity(MenuRecipe entity) {
    return MenuRecipeResponse.builder()
        .id(entity.getId())
        .cafeMenuPrice(CafeMenuPriceResponse.fromEntity(entity.getCafeMenuPrice()))
        .material(MaterialResponse.fromEntity(entity.getMaterial()))
        .usageAmount(entity.getUsageAmount())
        .unit(UnitResponse.fromEntity(entity.getUnit()))
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}
