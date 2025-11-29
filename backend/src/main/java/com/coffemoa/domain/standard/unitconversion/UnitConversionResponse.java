package com.coffemoa.domain.standard.unitconversion;

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
public class UnitConversionResponse {

  private Long id;
  private Long baseUnitId;
  private Long targetUnitId;
  private BigDecimal ratio;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
