package com.coffemoa.domain.finance.fixedcost;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedCostSearchRequest {

    private String costName;
    private String periodTypeCode;
    private Boolean active = true;
}
