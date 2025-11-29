package com.coffemoa.domain.standard.material;

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
public class MaterialCUDRequest {

  private Long id;
  private String name;
  private Long purchaseUnitId;
  private BigDecimal purchaseQuantity;
  private BigDecimal purchasePrice;

  private CUDFlag flag;
}
