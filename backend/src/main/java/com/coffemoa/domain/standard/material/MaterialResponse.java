package com.coffemoa.domain.standard.material;

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
public class MaterialResponse {

  private Long id;
  private String name;
  private Long purchaseUnitId;
  private UnitResponse purchaseUnit;
  private BigDecimal purchaseQuantity;
  private BigDecimal purchasePrice;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static MaterialResponse fromEntity(Material entity) {
    return MaterialResponse.builder()
        .id(entity.getId())
        .name(entity.getName())
        .purchaseUnitId(entity.getPurchaseUnit().getId())
        .purchaseUnit(UnitResponse.fromEntity(entity.getPurchaseUnit()))
        .purchaseQuantity(entity.getPurchaseQuantity())
        .purchasePrice(entity.getPurchasePrice())
        .createdAt(entity.getCreatedAt())
        .updatedAt(entity.getUpdatedAt())
        .build();
  }
}
