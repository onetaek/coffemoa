package com.coffemoa.domain.standard.material;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository
    extends JpaRepository<Material, Long>, MaterialQueryRepository {

}
