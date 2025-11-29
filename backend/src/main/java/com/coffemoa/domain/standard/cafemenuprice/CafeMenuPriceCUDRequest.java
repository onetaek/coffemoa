package com.coffemoa.domain.standard.cafemenuprice;

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
public class CafeMenuPriceCUDRequest {

  private Long id;
  private Long cafeMenuId;
  private String temperature;
  private String size;
  private BigDecimal price;

  private CUDFlag flag;

  public TemperatureOption getTemperature() {
    return TemperatureOption.fromCode(temperature);
  }

  public SizeOption getSize() {
    return SizeOption.fromCode(size);
  }
}
