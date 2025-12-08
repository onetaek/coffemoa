package com.coffemoa.domain.finance.recept;

import java.time.LocalDate;
import java.util.List;

public interface ReceiptQueryRepository {

  List<Receipt> searchByDateRange(ReceiptSearchRequest request);

  List<ReceiptDailyCountResponse> findDailyUploadCount(LocalDate startDate, LocalDate endDate);

}