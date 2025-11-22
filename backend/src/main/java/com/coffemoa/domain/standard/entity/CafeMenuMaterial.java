package com.coffemoa.domain.standard.entity;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CafeMenuMaterial {

  private String cafeMenuName;//메뉴명
  private String temperature;//온도구분
  private String size;//사이즈구분
  private String materialName;//원재료명
  private BigDecimal quantity;//사용량
  private String unitId;//단위명

}
