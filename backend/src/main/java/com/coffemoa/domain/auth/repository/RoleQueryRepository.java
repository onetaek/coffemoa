package com.coffemoa.domain.auth.repository;

import com.coffemoa.domain.auth.entity.QRole;
import com.coffemoa.domain.auth.entity.Role;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoleQueryRepository {

  private final JPAQueryFactory query;

  public Optional<Role> findByCode(String code) {
    QRole r = QRole.role;

    Role result = query
        .selectFrom(r)
        .where(
            r.code.eq(code),
            r.isActive.isTrue()
        )
        .fetchOne();

    return Optional.ofNullable(result);
  }

}