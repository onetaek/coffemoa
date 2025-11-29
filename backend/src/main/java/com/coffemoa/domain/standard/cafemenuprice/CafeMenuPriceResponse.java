package com.coffemoa.domain.standard.cafemenuprice;

import com.coffemoa.domain.standard.cafemenu.CafeMenuResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class CafeMenuPriceResponse {

  private Long id;
  private CafeMenuResponse cafeMenuPrice;
  private TemperatureOption temperature;
  private SizeOption size;
  private BigDecimal price;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static CafeMenuPriceResponse fromEntity(CafeMenuPrice entity) {
    return CafeMenuPriceResponse.builder()
        .id(entity.getId())
        .cafeMenuPrice(CafeMenuResponse.fromEntity(entity.getCafeMenu()))
        .temperature(entity.getTemperature())
        .size(entity.getSize())
        .price(entity.getPrice())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}
