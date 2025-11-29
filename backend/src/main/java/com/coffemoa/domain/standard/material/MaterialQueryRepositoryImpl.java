package com.coffemoa.domain.standard.material;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MaterialQueryRepositoryImpl implements MaterialQueryRepository {

  private final JPAQueryFactory query;

  @Override
  public List<Material> search(MaterialSearchRequest request) {

    QMaterial m = QMaterial.material;

    return query.selectFrom(m)
        .where(
            nameContains(request.getName()),
            unitEq(request.getPurchaseUnitId()),
            active(request.getActive())
        )
        .orderBy(m.createdAt.desc())
        .fetch();
  }

  private BooleanExpression nameContains(String name) {
    return (name != null && !name.isBlank())
        ? QMaterial.material.name.containsIgnoreCase(name)
        : null;
  }

  private BooleanExpression unitEq(Long unitId) {
    return unitId != null ? QMaterial.material.purchaseUnit.id.eq(unitId) : null;
  }

  private BooleanExpression active(Boolean active) {
    return active != null && active
        ? QMaterial.material.deletedAt.isNull()
        : null;
  }
}
