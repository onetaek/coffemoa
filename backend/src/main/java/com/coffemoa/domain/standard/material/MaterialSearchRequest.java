package com.coffemoa.domain.standard.material;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialSearchRequest {

  private String name;
  private Long purchaseUnitId;
  private Boolean active = true;
}
