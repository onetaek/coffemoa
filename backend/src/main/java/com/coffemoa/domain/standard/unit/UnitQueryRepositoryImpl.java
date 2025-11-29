package com.coffemoa.domain.standard.unit;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UnitQueryRepositoryImpl implements UnitQueryRepository {

  private final JPAQueryFactory query;

  @Override
  public List<Unit> search(UnitSearchRequest request) {
    QUnit unit = QUnit.unit;

    return query.selectFrom(unit)
        .where(
            nameContains(request.getName()),
            active(request.getActive())
        )
        .orderBy(unit.createdAt.desc())
        .fetch();
  }

  private BooleanExpression nameContains(String name) {
    return (name != null && !name.isBlank())
        ? QUnit.unit.name.containsIgnoreCase(name)
        : null;
  }

  private BooleanExpression active(Boolean active) {
    return active != null && active
        ? QUnit.unit.deletedAt.isNull()
        : null;
  }
}
