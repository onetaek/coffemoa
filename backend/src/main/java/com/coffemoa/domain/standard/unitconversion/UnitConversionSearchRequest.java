package com.coffemoa.domain.standard.unitconversion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitConversionSearchRequest {

  private Long baseUnitId;
  private Long targetUnitId;
  private Boolean active = true;
}
