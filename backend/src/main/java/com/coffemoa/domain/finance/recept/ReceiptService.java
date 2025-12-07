package com.coffemoa.domain.finance.recept;

import com.coffemoa.domain.finance.cafemenucost.CafeMenuCostResponse;
import com.coffemoa.domain.finance.cafemenucost.CafeMenuCostService;
import com.coffemoa.domain.finance.fixedcostrecord.FixedCostRecord;
import com.coffemoa.domain.finance.fixedcostrecord.FixedCostRecordService;
import io.micrometer.common.util.StringUtils;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
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
  private final FixedCostRecordService fixedCostRecordService;

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

  @Transactional(readOnly = true)
  public ReceiptCostResponse searchCost(ReceiptSearchRequest request) {

    LocalDate from = request.getFromDate();
    LocalDate to = request.getToDate();

    /* ================================
       1) 영수증 조회 + 원가 계산
       ================================ */
    List<Receipt> receipts = receiptRepository.searchByDateRange(request);

    List<CafeMenuCostResponse> menuCostList = cafeMenuCostService.getMenuCostList();
    Map<String, CafeMenuCostResponse> menuCodeMap = menuCostList.stream()
        .filter(v -> StringUtils.isNotBlank(v.getMenuCode()))
        .collect(Collectors.toMap(CafeMenuCostResponse::getMenuCode, v -> v));

    List<ReceiptCostResponse.Detail> detailList = new ArrayList<>();

    double totalTotalPrice = 0.0;
    double totalDiscountPrice = 0.0;
    double totalCostPrice = 0.0;

    for (Receipt r : receipts) {

      CafeMenuCostResponse cost = menuCodeMap.get(r.getProductCode());

      double costPrice = 0.0;
      if (cost != null) {
        String numeric = cost.getTotalCost().replaceAll("[^0-9.]", "");
        double costPerUnit = Double.parseDouble(numeric);
        costPrice = costPerUnit * r.getQuantity();
      }

      double profit = r.getActualPrice() - costPrice;

      totalTotalPrice += r.getTotalPrice();
      totalDiscountPrice += r.getDiscountPrice();
      totalCostPrice += costPrice;

      detailList.add(
          ReceiptCostResponse.Detail.builder()
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
              .costPrice(costPrice)
              .profitPrice(profit)
              .build()
      );
    }

    /* ================================
       2) 조회기간에 해당하는 고정비 가져오기
       ================================ */
    List<FixedCostRecord> fixedCosts =
        fixedCostRecordService.getApplicableFixedCosts(from, to);

    /* ================================
       3) 고정비 일할 계산
       ================================ */
    List<ReceiptCostResponse.FixedCostSummary> fixedCostList =
        fixedCosts.stream()
            .map(rec ->
                ReceiptCostResponse.FixedCostSummary.builder()
                    .fixedCostId(rec.getFixedCost().getId())
                    .costName(rec.getFixedCost().getCostName())
                    .periodTypeCode(rec.getFixedCost().getPeriodType().getCode())
                    .periodValue(rec.getPeriodValue())
                    .amount(rec.getAmount())
                    .appliedAmount(calculateAppliedCost(rec, from, to))
                    .build()
            )
            .toList();

    double totalFixedCost = fixedCostList.stream()
        .mapToDouble(ReceiptCostResponse.FixedCostSummary::getAppliedAmount)
        .sum();

    /* ================================
       4) 최종 수익 = 실매출 - 재료비 - 고정비
       ================================ */
    double totalActualPrice = receipts.stream()
        .mapToDouble(r -> r.getActualPrice().doubleValue())
        .sum();

    double totalProfit = totalActualPrice - totalCostPrice - totalFixedCost;

    /* ================================
       5) Response 구성
       ================================ */
    return ReceiptCostResponse.builder()
        .totalPrice(totalTotalPrice)
        .discountPrice(totalDiscountPrice)
        .costPrice(totalCostPrice)
        .profitPrice(totalProfit)
        .detailList(detailList)
        .fixedCostList(fixedCostList)
        .build();
  }

  /**
   * 고정비 일할 계산
   */
  private double calculateAppliedCost(FixedCostRecord rec, LocalDate from, LocalDate to) {

    String type = rec.getFixedCost().getPeriodType().getCode(); // YEARLY / MONTHLY / DAILY
    String pv = rec.getPeriodValue();
    double amount = rec.getAmount();

    switch (type) {

      case "DAILY":
        LocalDate day = LocalDate.parse(pv);
        return (day.isBefore(from) || day.isAfter(to)) ? 0 : amount;

      case "MONTHLY":
        return calculateMonthly(rec, from, to);

      case "YEARLY":
        return calculateYearly(rec, from, to);

      default:
        return 0;
    }
  }

  /**
   * 월 단위 고정비
   */
  private double calculateMonthly(FixedCostRecord rec, LocalDate from, LocalDate to) {

    YearMonth ym = YearMonth.parse(rec.getPeriodValue()); // ex) 2025-07
    LocalDate monthStart = ym.atDay(1);
    LocalDate monthEnd = ym.atEndOfMonth();

    LocalDate start = from.isAfter(monthStart) ? from : monthStart;
    LocalDate end = to.isBefore(monthEnd) ? to : monthEnd;

    if (end.isBefore(start)) {
      return 0;
    }

    long usedDays = ChronoUnit.DAYS.between(start, end.plusDays(1));
    long totalDays = monthEnd.getDayOfMonth();

    return rec.getAmount() * ((double) usedDays / totalDays);
  }

  /**
   * 연 단위 고정비
   */
  private double calculateYearly(FixedCostRecord rec, LocalDate from, LocalDate to) {

    int year = Integer.parseInt(rec.getPeriodValue());
    LocalDate yearStart = LocalDate.of(year, 1, 1);
    LocalDate yearEnd = LocalDate.of(year, 12, 31);

    LocalDate start = from.isAfter(yearStart) ? from : yearStart;
    LocalDate end = to.isBefore(yearEnd) ? to : yearEnd;

    if (end.isBefore(start)) {
      return 0;
    }

    long usedDays = ChronoUnit.DAYS.between(start, end.plusDays(1));
    long totalDays = yearStart.lengthOfYear();

    return rec.getAmount() * ((double) usedDays / totalDays);
  }


}
