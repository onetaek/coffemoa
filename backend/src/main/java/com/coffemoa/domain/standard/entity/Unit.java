package com.coffemoa.domain.standard.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Unit {

  private String unitId;//단위명
  private String description;//설명

}
