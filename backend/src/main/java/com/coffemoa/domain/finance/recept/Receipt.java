package com.coffemoa.domain.finance.recept;

import com.coffemoa.global.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "receipt"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Receipt extends BaseAuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 조회일자 (파일 상단의 date)
  @Column(name = "sales_date", nullable = false)
  private LocalDate salesDate;

  // 포스 번호
  @Column(length = 10)
  private String posNumber;

  // 영수증번호
  @Column(length = 20)
  private String receiptNumber;

  // 매출/환불 등의 구분
  @Column(length = 20)
  private String category;

  // 최초주문시각
  @Column(length = 10)
  private String orderTime;

  // 결제시각
  @Column(length = 10)
  private String payTime;

  // 상품코드
  @Column(length = 20)
  private String productCode;

  // 상품명
  @Column(length = 100)
  private String productName;

  // 수량
  private Integer quantity;

  // 총 매출액
  private Integer totalPrice;

  // 할인액
  private Integer discountPrice;

  // 실매출액
  private Integer actualPrice;

  // 가액(원가계산)
  private Integer cost;

  // 부가세
  private Integer vat;
}
