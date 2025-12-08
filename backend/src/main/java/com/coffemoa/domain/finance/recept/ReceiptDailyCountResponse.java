package com.coffemoa.domain.finance.recept;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReceiptDailyCountResponse {

  private LocalDate date;
  private Long count;
}