package com.coffemoa.domain.standard.unitconversion;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UnitConversionRepository
    extends JpaRepository<UnitConversion, Long>, UnitConversionQueryRepository {

}
