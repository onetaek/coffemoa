package com.coffemoa.domain.finance.fixedcostrecord;

import java.util.List;

public interface FixedCostRecordQueryRepository {

  List<FixedCostRecord> search(FixedCostRecordSearchRequest request);
}
