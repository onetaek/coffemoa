package com.coffemoa.domain.finance.fixedcost;

import java.util.List;

public interface FixedCostQueryRepository {
    List<FixedCost> search(FixedCostSearchRequest request);
}
