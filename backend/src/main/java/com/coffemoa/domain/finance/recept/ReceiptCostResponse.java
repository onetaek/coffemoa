package com.coffemoa.domain.finance.recept;

import java.time.LocalDate;
import java.util.List;
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
public class ReceiptCostResponse {

  private Double totalPrice;//매출액
  private Double discountPrice;//할인액
  private Double costPrice;//재료비
  private Double profitPrice;//수익금
  private List<Detail> detailList;

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Detail {

    private Long id;
    private LocalDate salesDate;
    private String receiptNumber;
    private String orderTime;
    private String productCode;
    private String productName;
    private Double quantity;
    private Double totalPrice;//매출액
    private Double discountPrice;//할인액
    private Double actualPrice;
    private Double costPrice;//재료비
    private Double profitPrice;// 수익금
  }
}
