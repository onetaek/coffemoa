package com.coffemoa.domain.auth.repository;

import com.coffemoa.domain.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

}
