package com.coffemoa.domain.finance.fixedcost;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FixedCostPeriodType {

  YEARLY("YEARLY", "연 단위", 1),
  MONTHLY("MONTHLY", "월 단위", 2),
  WEEKLY("WEEKLY", "주 단위", 3),
  DAILY("DAILY", "일 단위", 4);

  private final String code;
  private final String displayName;
  private final int sequence;

  public static FixedCostPeriodType fromCode(String code) {
    for (FixedCostPeriodType type : values()) {
      if (type.getCode().equalsIgnoreCase(code)) {
        return type;
      }
    }
    return null;
  }
}
