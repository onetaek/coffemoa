package com.coffemoa.domain.standard.cafemenu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CafeMenuSearchRequest {

  private String name;
  private Boolean active = true;
}
