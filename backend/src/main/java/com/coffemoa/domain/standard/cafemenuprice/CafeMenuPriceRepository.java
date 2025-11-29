package com.coffemoa.domain.standard.cafemenuprice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeMenuPriceRepository
    extends JpaRepository<CafeMenuPrice, Long>, CafeMenuPriceQueryRepository {

}
