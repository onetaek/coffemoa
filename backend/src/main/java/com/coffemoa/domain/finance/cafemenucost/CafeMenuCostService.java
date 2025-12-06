package com.coffemoa.domain.finance.cafemenucost;

import com.coffemoa.domain.finance.cafemenucost.CafeMenuCostResponse.CafeMenuCostDetail;
import com.coffemoa.global.utils.BigDecimalUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeMenuCostService {

  private final CafeMenuCostQueryRepository repository;

  public List<CafeMenuCostResponse> getMenuCostList() {

    List<CafeMenuCostRow> rows = repository.findMenuCostRows();

    // cafeMenuPriceId 기준으로 Grouping
    Map<Long, List<CafeMenuCostRow>> grouped = rows.stream()
        .collect(Collectors.groupingBy(CafeMenuCostRow::getCafeMenuPriceId));

    List<CafeMenuCostResponse> result = new ArrayList<>();

    for (Map.Entry<Long, List<CafeMenuCostRow>> entry : grouped.entrySet()) {
      List<CafeMenuCostRow> groupRows = entry.getValue();
      CafeMenuCostRow master = groupRows.get(0); // Master 요소

      // Detail 리스트 생성
      List<CafeMenuCostDetail> recipeList = groupRows.stream()
          .map(r -> CafeMenuCostDetail.builder()
              .materialName(r.getMaterialName())
              .materialUsageAmount(String.valueOf(r.getUsageAmount()))
              .materialUnitName(r.getUnitName())
              .materialCost(String.format("%.2f", r.getMaterialCost()))
              .build())
          .toList();

      // Master 생성
      BigDecimal totalCost = groupRows.stream()
          .map(CafeMenuCostRow::getMaterialCost)
          .filter(Objects::nonNull)
          .reduce(BigDecimal.ZERO, BigDecimal::add);

      BigDecimal menuPrice = master.getMenuPrice();

      BigDecimal costRate = totalCost
          .divide(menuPrice, 4, RoundingMode.HALF_UP)   // 4자리 소수
          .multiply(new BigDecimal("100"));

      String recipeListFlat = recipeList.stream()
          .map(r -> String.format(
              "%s %s%s(%s원)",
              r.getMaterialName(),
              BigDecimalUtil.format(new BigDecimal(r.getMaterialUsageAmount()), 2),
              r.getMaterialUnitName(),
              BigDecimalUtil.format(new BigDecimal(r.getMaterialCost()), 2)
          ))
          .collect(Collectors.joining(" + "));

      CafeMenuCostResponse dto = CafeMenuCostResponse.builder()
          .menuCode(master.getCafeMenuCode())
          .menuName(master.getMenuName())
          .temperature(master.getTemperature())
          .size(master.getSize())
          .menuPrice(BigDecimalUtil.format(menuPrice, 0))
          .totalCost(BigDecimalUtil.format(totalCost, 0))
          .costRate(BigDecimalUtil.format(costRate, 2))
          // .recipeList(recipeList)
          .recipeListFlat(recipeListFlat)
          .build();

      result.add(dto);
    }

    return result;
  }
}