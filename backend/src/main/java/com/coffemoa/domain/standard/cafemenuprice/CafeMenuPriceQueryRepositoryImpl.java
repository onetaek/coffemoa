package com.coffemoa.domain.standard.cafemenuprice;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CafeMenuPriceQueryRepositoryImpl implements CafeMenuPriceQueryRepository {

  private final JPAQueryFactory query;

  @Override
  public List<CafeMenuPrice> search(CafeMenuPriceSearchRequest request) {
    QCafeMenuPrice p = QCafeMenuPrice.cafeMenuPrice;

    return query.selectFrom(p)
        .where(
            cafeMenuEq(request.getCafeMenuId()),
            tempEq(request.getTemperature()),
            sizeEq(request.getSize()),
            active(request.getActive())
        )
        .orderBy(p.createdAt.desc())
        .fetch();
  }

  private BooleanExpression cafeMenuEq(Long id) {
    return id != null ? QCafeMenuPrice.cafeMenuPrice.cafeMenu.id.eq(id) : null;
  }

  private BooleanExpression tempEq(TemperatureOption t) {
    return t != null ? QCafeMenuPrice.cafeMenuPrice.temperature.eq(t) : null;
  }

  private BooleanExpression sizeEq(SizeOption s) {
    return s != null ? QCafeMenuPrice.cafeMenuPrice.size.eq(s) : null;
  }

  private BooleanExpression active(Boolean active) {
    return active != null && active
        ? QCafeMenuPrice.cafeMenuPrice.deletedAt.isNull()
        : null;
  }
}
