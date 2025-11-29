package com.coffemoa.domain.standard.unit;

import java.util.List;

public interface UnitQueryRepository {

  List<Unit> search(UnitSearchRequest request);
}
