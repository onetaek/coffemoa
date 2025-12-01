package com.coffemoa.domain.finance.cafemenucost;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeMenuCostRow {

  // Master 필드
  private Long cafeMenuPriceId;
  private String menuName;
  private String temperature;
  private String size;
  private BigDecimal menuPrice;

  // Detail 필드
  private String materialName;
  private BigDecimal usageAmount;
  private String unitName;
  private BigDecimal materialCost;
}