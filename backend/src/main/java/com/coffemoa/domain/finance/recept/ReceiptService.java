package com.coffemoa.domain.finance.recept;

import com.coffemoa.domain.finance.cafemenucost.CafeMenuCostResponse;
import com.coffemoa.domain.finance.cafemenucost.CafeMenuCostService;
import io.micrometer.common.util.StringUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReceiptService {

  private final CafeMenuCostService cafeMenuCostService;

  private final ReceiptRepository receiptRepository;

  public void uploadReceipt(ReceiptUploadRequest request) {
    LocalDate salesDate = LocalDate.parse(request.getDate());

    // 1) 기존 데이터 조회
    List<Receipt> existing = receiptRepository.findAllBySalesDate(salesDate);

    // 2) 기존 데이터가 있다면 soft delete
    if (!existing.isEmpty()) {
      existing.forEach(Receipt::markDeleted);
      receiptRepository.saveAll(existing);
    }

    // 3) 새로운 데이터 저장
    List<Receipt> newReceipts = request.getReceiptList().stream()
        .map(item -> Receipt.builder()
            .salesDate(salesDate)
            .posNumber(item.getPosNumber())
            .receiptNumber(item.getReceiptNumber())
            .category(item.getCategory())
            .orderTime(item.getOrderTime())
            .payTime(item.getPayTime())
            .productCode(item.getProductCode())
            .productName(item.getProductName())
            .quantity(item.getQuantity())
            .totalPrice(item.getTotalPrice())
            .discountPrice(item.getDiscountPrice())
            .actualPrice(item.getActualPrice())
            .cost(item.getCost())
            .vat(item.getVat())
            .build())
        .toList();

    receiptRepository.saveAll(newReceipts);
  }


  @Transactional(readOnly = true)
  public List<ReceiptResponse> search(ReceiptSearchRequest request) {
    return receiptRepository.searchByDateRange(request).stream()
        .map(ReceiptResponse::fromEntity)
        .toList();
  }

  public ReceiptCostResponse searchCost(ReceiptSearchRequest request) {

    List<Receipt> receipts = receiptRepository.searchByDateRange(request);

    // 메뉴코드 → 원가정보 맵
    List<CafeMenuCostResponse> menuCostList = cafeMenuCostService.getMenuCostList();
    Map<String, CafeMenuCostResponse> menuCodeMap = menuCostList.stream()
        .filter(v -> StringUtils.isNotBlank(v.getMenuCode()))
        .collect(Collectors.toMap(CafeMenuCostResponse::getMenuCode, v -> v));

    List<ReceiptCostResponse.Detail> detailList = new ArrayList<>();

    // 총합 계산 변수(Double)
    double totalTotalPrice = 0.0;
    double totalDiscountPrice = 0.0;
    double totalCostPrice = 0.0;

    for (Receipt r : receipts) {
      CafeMenuCostResponse cost = menuCodeMap.get(r.getProductCode());

      // 원가 계산
      double costPrice = 0.0;
      if (cost != null) {
        // 예: "1216.67원" → 1216.67
        String numeric = cost.getTotalCost().replaceAll("[^0-9.]", "");
        double costPerUnit = Double.parseDouble(numeric);

        costPrice = costPerUnit * r.getQuantity();
      }

      // 수익금 계산
      double profit = r.getActualPrice() - costPrice;

      // 전체 합계 누적
      totalTotalPrice += r.getTotalPrice();
      totalDiscountPrice += r.getDiscountPrice();
      totalCostPrice += costPrice;

      // Detail 생성
      ReceiptCostResponse.Detail detail = ReceiptCostResponse.Detail.builder()
          .id(r.getId())
          .salesDate(r.getSalesDate())
          .receiptNumber(r.getReceiptNumber())
          .orderTime(r.getOrderTime())
          .productCode(r.getProductCode())
          .productName(r.getProductName())
          .quantity(r.getQuantity().doubleValue())
          .totalPrice(r.getTotalPrice().doubleValue())
          .discountPrice(r.getDiscountPrice().doubleValue())
          .actualPrice(r.getActualPrice().doubleValue())
          .costPrice(r.getCost().doubleValue())
          .profitPrice(profit)
          .build();

      detailList.add(detail);
    }

    // 전체 수익금 계산 = 전체 실매출 - 전체 원가
    double totalActualPrice = receipts.stream()
        .mapToDouble(r -> r.getActualPrice().doubleValue())
        .sum();

    double totalProfit = totalActualPrice - totalCostPrice;

    return ReceiptCostResponse.builder()
        .totalPrice(totalTotalPrice)
        .discountPrice(totalDiscountPrice)
        .costPrice(totalCostPrice)
        .profitPrice(totalProfit)
        .detailList(detailList)
        .build();
  }

}
