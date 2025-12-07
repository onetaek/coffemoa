package com.coffemoa.domain.finance.fixedcost;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedCostResponse {

    private Long id;
    private String costName;
    private String periodTypeCode;
    private String periodTypeName;
    private String remark;

    public static FixedCostResponse fromEntity(FixedCost f) {
        return FixedCostResponse.builder()
                .id(f.getId())
                .costName(f.getCostName())
                .periodTypeCode(f.getPeriodType().getCode())
                .periodTypeName(f.getPeriodType().getDisplayName())
                .remark(f.getRemark())
                .build();
    }
}
