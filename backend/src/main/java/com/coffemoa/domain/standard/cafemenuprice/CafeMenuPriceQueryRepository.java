package com.coffemoa.domain.standard.cafemenuprice;

import java.util.List;

public interface CafeMenuPriceQueryRepository {

  List<CafeMenuPrice> search(CafeMenuPriceSearchRequest request);
}
