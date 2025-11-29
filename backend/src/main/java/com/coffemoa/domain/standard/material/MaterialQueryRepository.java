package com.coffemoa.domain.standard.material;

import java.util.List;

public interface MaterialQueryRepository {

  List<Material> search(MaterialSearchRequest request);
}
