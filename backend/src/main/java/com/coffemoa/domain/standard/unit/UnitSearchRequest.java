package com.coffemoa.domain.standard.unit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitSearchRequest {

  private String name;
  private Boolean active = true;
}
