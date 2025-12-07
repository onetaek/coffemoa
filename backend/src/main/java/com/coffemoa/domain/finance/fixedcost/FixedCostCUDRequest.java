package com.coffemoa.domain.finance.fixedcost;

import com.coffemoa.global.CUDFlag;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedCostCUDRequest {

    private Long id;
    private String costName;
    private String periodTypeCode;
    private String remark;

    private CUDFlag flag;
}
