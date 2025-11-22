package com.coffemoa.domain.standard.entity;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UnitConversion {

  private String standardUnitId;//기준단위명
  private String conversionUnitId;//변환단위명
  private BigDecimal conversionQuantity;//환산값

}
