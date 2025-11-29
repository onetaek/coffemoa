package com.coffemoa.domain.standard.menurecipe;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MenuRecipeQueryRepositoryImpl implements MenuRecipeQueryRepository {

  private final JPAQueryFactory query;

  @Override
  public List<MenuRecipe> search(MenuRecipeSearchRequest request) {

    QMenuRecipe r = QMenuRecipe.menuRecipe;

    return query.selectFrom(r)
        .where(
            cafeMenuPriceEq(request.getCafeMenuPriceId()),
            materialEq(request.getMaterialId()),
            active(request.getActive())
        )
        .orderBy(r.createdAt.desc())
        .fetch();
  }

  private BooleanExpression cafeMenuPriceEq(Long id) {
    return id != null ? QMenuRecipe.menuRecipe.cafeMenuPrice.id.eq(id) : null;
  }

  private BooleanExpression materialEq(Long id) {
    return id != null ? QMenuRecipe.menuRecipe.material.id.eq(id) : null;
  }

  private BooleanExpression active(Boolean active) {
    return active != null && active
        ? QMenuRecipe.menuRecipe.deletedAt.isNull()
        : null;
  }
}
