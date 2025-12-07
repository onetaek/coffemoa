package com.coffemoa.domain.finance.fixedcostrecord;

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
public class FixedCostRecordResponse {

  private Long id;
  private Long fixedCostId;
  private String fixedCostName;
  private String periodTypeCode;
  private String periodTypeName;
  private String periodValue;
  private Double amount;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static FixedCostRecordResponse fromEntity(FixedCostRecord e) {
    return FixedCostRecordResponse.builder()
        .id(e.getId())
        .fixedCostId(e.getFixedCost().getId())
        .fixedCostName(e.getFixedCost().getCostName())
        .periodTypeCode(e.getFixedCost().getPeriodType().getCode())
        .periodTypeName(e.getFixedCost().getPeriodType().getDisplayName())
        .periodValue(e.getPeriodValue())
        .amount(e.getAmount())
        .createdAt(e.getCreatedAt())
        .updatedAt(e.getUpdatedAt())
        .build();
  }
}

