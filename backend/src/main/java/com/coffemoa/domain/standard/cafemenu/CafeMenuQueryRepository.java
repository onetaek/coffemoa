package com.coffemoa.domain.standard.cafemenu;

import java.util.List;

public interface CafeMenuQueryRepository {

  List<CafeMenu> search(CafeMenuSearchRequest request);
}
