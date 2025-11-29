package com.coffemoa.domain.standard.cafemenu;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CafeMenuQueryRepositoryImpl implements CafeMenuQueryRepository {

  private final JPAQueryFactory query;

  @Override
  public List<CafeMenu> search(CafeMenuSearchRequest request) {
    QCafeMenu m = QCafeMenu.cafeMenu;

    return query.selectFrom(m)
        .where(
            nameContains(request.getName()),
            active(request.getActive())
        )
        .orderBy(m.createdAt.desc())
        .fetch();
  }

  private BooleanExpression nameContains(String name) {
    return (name != null && !name.isBlank())
        ? QCafeMenu.cafeMenu.name.containsIgnoreCase(name)
        : null;
  }

  private BooleanExpression active(Boolean active) {
    return active != null && active
        ? QCafeMenu.cafeMenu.deletedAt.isNull()
        : null;
  }
}
