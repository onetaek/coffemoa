package com.coffemoa.domain.finance.fixedcost;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FixedCostQueryRepositoryImpl implements FixedCostQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public List<FixedCost> search(FixedCostSearchRequest request) {

        QFixedCost fc = QFixedCost.fixedCost;

        return query.selectFrom(fc)
                .where(
                        costNameContains(request.getCostName()),
                        periodTypeEq(request.getPeriodTypeCode()),
                        active(request.getActive())
                )
                .orderBy(fc.createdAt.desc())
                .fetch();
    }

    private BooleanExpression costNameContains(String name) {
        return (name != null && !name.isBlank())
                ? QFixedCost.fixedCost.costName.containsIgnoreCase(name)
                : null;
    }

    private BooleanExpression periodTypeEq(String code) {
        return (code != null)
                ? QFixedCost.fixedCost.periodType.eq(FixedCostPeriodType.fromCode(code))
                : null;
    }

    private BooleanExpression active(Boolean active) {
        return active != null && active
                ? QFixedCost.fixedCost.deletedAt.isNull()
                : null;
    }
}
