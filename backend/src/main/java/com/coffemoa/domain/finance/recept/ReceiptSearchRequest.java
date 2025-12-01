package com.coffemoa.domain.finance.recept;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiptSearchRequest {

  private LocalDate fromDate;   // 조회 시작일
  private LocalDate toDate;     // 조회 종료일

}