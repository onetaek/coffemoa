package com.coffemoa.domain.finance.recept;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReceiptService {

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

}
