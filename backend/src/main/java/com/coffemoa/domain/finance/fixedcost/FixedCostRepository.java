package com.coffemoa.domain.finance.fixedcost;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FixedCostRepository
        extends JpaRepository<FixedCost, Long>, FixedCostQueryRepository {
}
