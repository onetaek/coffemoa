package com.coffemoa.domain.standard.menurecipe;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuRecipeSearchRequest {

  private Long cafeMenuPriceId;
  private Long materialId;
  private Boolean active = true;
}
