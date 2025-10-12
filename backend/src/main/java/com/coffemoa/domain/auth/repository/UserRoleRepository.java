package com.coffemoa.domain.auth.repository;

import com.coffemoa.domain.auth.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

}
