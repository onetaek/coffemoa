package com.coffemoa.domain.finance.fixedcostrecord;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedCostRecordSearchRequest {

  private Long fixedCostId;
  private String from;
  private String to;
  private Boolean active = true;
}
