package com.coffemoa.domain.standard.cafemenuprice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TemperatureOption {
  H("H", "Hot", 1),
  I("I", "Ice", 2);

  private final String code;
  private final String displayName;
  private final int sequence;

  public static TemperatureOption fromCode(String code) {
    for (TemperatureOption temperatureOption : values()) {
      if (temperatureOption.getCode().equals(code)) {
        return temperatureOption;
      }
    }
    return null;
  }
}