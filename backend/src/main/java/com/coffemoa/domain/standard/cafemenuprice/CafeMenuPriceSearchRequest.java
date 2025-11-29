package com.coffemoa.domain.standard.cafemenuprice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CafeMenuPriceSearchRequest {

  private Long cafeMenuId;
  private String temperature;
  private String size;
  private Boolean active = true;

  public TemperatureOption getTemperature() {
    return TemperatureOption.fromCode(temperature);
  }

  public SizeOption getSize() {
    return SizeOption.fromCode(size);
  }
}
