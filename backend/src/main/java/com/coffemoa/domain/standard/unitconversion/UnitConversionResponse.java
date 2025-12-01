package com.coffemoa.domain.standard.unitconversion;

import com.coffemoa.domain.standard.unit.UnitResponse;
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
  private UnitResponse baseUnit;
  private UnitResponse targetUnit;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static UnitConversionResponse fromEntity(UnitConversion entity) {
    return UnitConversionResponse.builder()
        .id(entity.getId())
        .baseUnitId(entity.getBaseUnit().getId())
        .targetUnitId(entity.getTargetUnit().getId())
        .baseUnit(UnitResponse.fromEntity(entity.getBaseUnit()))
        .targetUnit(UnitResponse.fromEntity(entity.getTargetUnit()))
        .ratio(entity.getRatio())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}
