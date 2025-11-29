package com.coffemoa.domain.standard.unitconversion;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UnitConversionQueryRepositoryImpl implements UnitConversionQueryRepository {

  private final JPAQueryFactory query;

  @Override
  public List<UnitConversion> search(UnitConversionSearchRequest request) {
    QUnitConversion uc = QUnitConversion.unitConversion;

    return query.selectFrom(uc)
        .where(
            eqBaseUnit(request.getBaseUnitId()),
            eqTargetUnit(request.getTargetUnitId()),
            active(request.getActive())
        )
        .orderBy(uc.createdAt.desc())
        .fetch();
  }

  private BooleanExpression eqBaseUnit(Long baseUnitId) {
    return baseUnitId != null ? QUnitConversion.unitConversion.baseUnit.id.eq(baseUnitId) : null;
  }

  private BooleanExpression eqTargetUnit(Long targetUnitId) {
    return targetUnitId != null ? QUnitConversion.unitConversion.targetUnit.id.eq(targetUnitId)
        : null;
  }

  private BooleanExpression active(Boolean active) {
    return active != null && active
        ? QUnitConversion.unitConversion.deletedAt.isNull()
        : null;
  }
}
