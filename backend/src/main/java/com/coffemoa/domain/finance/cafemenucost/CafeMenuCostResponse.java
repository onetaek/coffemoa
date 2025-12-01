package com.coffemoa.domain.finance.cafemenucost;

import java.util.List;
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
public class CafeMenuCostResponse {

  private String menuName;
  private String temperature;
  private String size;
  private String totalCost;
  private String menuPrice;
  private String costRate;

  private List<CafeMenuCostDetail> recipeList;
  private String recipeListFlat;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class CafeMenuCostDetail {

    private String materialName;
    private String materialUsageAmount;
    private String materialUnitName;
    private String materialCost;
  }
}
