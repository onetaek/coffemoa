package com.coffemoa.domain.finance.fixedcostrecord;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FixedCostRecordRepository
    extends JpaRepository<FixedCostRecord, Long>, FixedCostRecordQueryRepository {

}
