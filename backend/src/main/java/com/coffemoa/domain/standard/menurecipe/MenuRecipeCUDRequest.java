package com.coffemoa.domain.standard.menurecipe;

import com.coffemoa.global.CUDFlag;
import java.math.BigDecimal;
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
public class MenuRecipeCUDRequest {

  private Long id;
  private Long cafeMenuPriceId;
  private Long materialId;
  private BigDecimal usageAmount;
  private Long unitId;

  private CUDFlag flag;
}
