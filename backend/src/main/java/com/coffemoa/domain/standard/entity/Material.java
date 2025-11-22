package com.coffemoa.domain.standard.entity;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Material {

  private String materialName;//원재료명
  private BigDecimal quantity;//구매단위수량
  private String unitId;//구매단위명
  private BigDecimal price;//구매단위가격

}
