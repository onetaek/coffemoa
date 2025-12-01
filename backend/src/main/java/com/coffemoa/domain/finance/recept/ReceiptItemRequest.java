package com.coffemoa.domain.finance.recept;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiptItemRequest {

  private String posNumber;      // posNo  → posNumber
  private String receiptNumber;  // receiptNo → receiptNumber
  private String category;

  private String orderTime;
  private String payTime;

  private String productCode;
  private String productName;

  private Integer quantity;

  private Integer totalPrice;

  private Integer discountPrice;   // discountAmount → discountPrice
  private Integer actualPrice;     // netSales → actualPrice

  private Integer cost;
  private Integer vat;
}