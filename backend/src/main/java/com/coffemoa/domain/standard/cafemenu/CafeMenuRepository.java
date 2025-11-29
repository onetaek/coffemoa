package com.coffemoa.domain.standard.cafemenu;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeMenuRepository
    extends JpaRepository<CafeMenu, Long>, CafeMenuQueryRepository {

}
