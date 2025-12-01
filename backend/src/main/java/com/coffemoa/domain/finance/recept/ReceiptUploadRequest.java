package com.coffemoa.domain.finance.recept;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiptUploadRequest {

  private String date;   // salesDate와 연결되는 raw string (yyyy-MM-dd)
  private List<ReceiptItemRequest> receiptList;
}