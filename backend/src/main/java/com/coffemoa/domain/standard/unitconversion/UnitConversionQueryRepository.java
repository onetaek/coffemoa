package com.coffemoa.domain.standard.unitconversion;

import java.util.List;

public interface UnitConversionQueryRepository {

  List<UnitConversion> search(UnitConversionSearchRequest request);
}
