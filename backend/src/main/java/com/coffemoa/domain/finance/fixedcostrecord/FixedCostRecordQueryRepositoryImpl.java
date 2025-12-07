package com.coffemoa.domain.finance.fixedcostrecord;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FixedCostRecordQueryRepositoryImpl implements FixedCostRecordQueryRepository {

  private final JPAQueryFactory query;

  @Override
  public List<FixedCostRecord> search(FixedCostRecordSearchRequest request) {

    QFixedCostRecord r = QFixedCostRecord.fixedCostRecord;

    return query.selectFrom(r)
        .where(
            fixedCostEq(request.getFixedCostId()),
            dateOverlap(request.getFrom(), request.getTo()),
            active(request.getActive())
        )
        .orderBy(r.periodValue.asc())
        .fetch();
  }

  private BooleanExpression fixedCostEq(Long fixedCostId) {
    return fixedCostId != null ? QFixedCostRecord.fixedCostRecord.fixedCost.id.eq(fixedCostId)
        : null;
  }

  private BooleanExpression dateOverlap(String from, String to) {
    if (from == null || to == null) {
      return null;
    }
    return QFixedCostRecord.fixedCostRecord.periodValue.loe(to)
        .and(QFixedCostRecord.fixedCostRecord.periodValue.goe(from));
  }

  private BooleanExpression active(Boolean active) {
    return active != null && active
        ? QFixedCostRecord.fixedCostRecord.deletedAt.isNull()
        : null;
  }
}
