package com.coffemoa.domain.finance.cafemenucost;

import com.coffemoa.domain.standard.cafemenu.QCafeMenu;
import com.coffemoa.domain.standard.cafemenuprice.QCafeMenuPrice;
import com.coffemoa.domain.standard.material.QMaterial;
import com.coffemoa.domain.standard.menurecipe.QMenuRecipe;
import com.coffemoa.domain.standard.unit.QUnit;
import com.coffemoa.domain.standard.unitconversion.QUnitConversion;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CafeMenuCostQueryRepository {

  private final JPAQueryFactory query;

  public List<CafeMenuCostRow> findMenuCostRows() {
    QMenuRecipe mr = QMenuRecipe.menuRecipe;
    QCafeMenuPrice mp = QCafeMenuPrice.cafeMenuPrice;
    QCafeMenu cm = QCafeMenu.cafeMenu;
    QMaterial mat = QMaterial.material;
    QUnit u = QUnit.unit;
    QUnitConversion uc = QUnitConversion.unitConversion;

    return query
        .select(Projections.fields(
            CafeMenuCostRow.class,
            mp.id.as("cafeMenuPriceId"),
            cm.name.as("menuName"),
            mp.temperature.stringValue().as("temperature"),
            mp.size.stringValue().as("size"),
            mp.price.as("menuPrice"),

            mat.name.as("materialName"),
            mr.usageAmount.as("usageAmount"),
            u.name.as("unitName"),

            // materialCost = usageAmount * ratio * (purchase_price / purchase_quantity)
            Expressions.numberTemplate(BigDecimal.class,
                "({0} * COALESCE({1}, 1) * ({2} / NULLIF({3}, 0)))",
                mr.usageAmount,
                uc.ratio,
                mat.purchasePrice,
                mat.purchaseQuantity
            ).as("materialCost")
        ))
        .from(mr)
        .join(mp).on(mr.cafeMenuPrice.id.eq(mp.id))
        .join(cm).on(mp.cafeMenu.id.eq(cm.id))
        .join(mat).on(mr.material.id.eq(mat.id))
        .join(u).on(mr.unit.id.eq(u.id))
        .leftJoin(uc).on(
            uc.baseUnit.id.eq(mr.unit.id),
            uc.targetUnit.id.eq(mat.purchaseUnit.id)
        )
        .fetch();
  }
}
