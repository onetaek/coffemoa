package com.coffemoa.domain.finance.recept;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptResponse {

  private Long id;
  private LocalDate salesDate;
  private String posNumber;
  private String receiptNumber;
  private String category;
  private String orderTime;
  private String payTime;
  private String productCode;
  private String productName;
  private Integer quantity;
  private Integer totalPrice;
  private Integer discountPrice;
  private Integer actualPrice;
  private Integer cost;
  private Integer vat;

  public static ReceiptResponse fromEntity(Receipt r) {
    return ReceiptResponse.builder()
        .id(r.getId())
        .salesDate(r.getSalesDate())
        .posNumber(r.getPosNumber())
        .receiptNumber(r.getReceiptNumber())
        .category(r.getCategory())
        .orderTime(r.getOrderTime())
        .payTime(r.getPayTime())
        .productCode(r.getProductCode())
        .productName(r.getProductName())
        .quantity(r.getQuantity())
        .totalPrice(r.getTotalPrice())
        .discountPrice(r.getDiscountPrice())
        .actualPrice(r.getActualPrice())
        .cost(r.getCost())
        .vat(r.getVat())
        .build();
  }
}
