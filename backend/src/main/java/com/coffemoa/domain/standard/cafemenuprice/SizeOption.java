package com.coffemoa.domain.standard.cafemenuprice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SizeOption {
  S("S", "Small", 1),
  R("R", "Regular", 2),
  L("L", "Large", 3);

  private final String code;
  private final String displayName;
  private final int sequence;

  public static SizeOption fromCode(String code) {
    for (SizeOption sizeOption : values()) {
      if (sizeOption.getCode().equals(code)) {
        return sizeOption;
      }
    }
    return null;
  }
}
