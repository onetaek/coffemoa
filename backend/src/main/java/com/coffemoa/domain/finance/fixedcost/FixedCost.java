package com.coffemoa.domain.finance.fixedcost;

import com.coffemoa.global.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fixed_cost")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FixedCost extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String costName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FixedCostPeriodType periodType;

    private String remark;
}
