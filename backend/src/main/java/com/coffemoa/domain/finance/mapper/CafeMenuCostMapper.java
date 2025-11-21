package com.coffemoa.domain.finance.mapper;

import com.coffemoa.domain.finance.entity.CafeMenuCost;
import java.util.List;
import java.util.Map;

public interface CafeMenuCostMapper {

  List<CafeMenuCost> select(Map<String, String> param);
}
