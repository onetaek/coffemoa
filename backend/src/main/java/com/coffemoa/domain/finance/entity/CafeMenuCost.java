package com.coffemoa.domain.finance.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeMenuCost {

  private String menuName;         // 메뉴명
  private String temperatureType;  // 온도구분 (H/I)
  private String sizeType;         // 사이즈구분 (R/L)
  private String recipe;           // 레시피
  private String totalCost;        // 총원가 (문자열로 들어온다고 했으므로 String)
  private String price;            // 판매가
  private Double costRate;         // 원가율 (decimal(46,2))
}