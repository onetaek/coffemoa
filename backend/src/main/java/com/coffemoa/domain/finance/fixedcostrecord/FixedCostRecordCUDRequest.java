package com.coffemoa.domain.finance.fixedcostrecord;

import com.coffemoa.global.CUDFlag;
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
public class FixedCostRecordCUDRequest {

  private Long id;
  private Long fixedCostId;
  private String periodValue; // YEAR/MONTH/DAY 공통 적용
  private Double amount;

  private CUDFlag flag;
}
