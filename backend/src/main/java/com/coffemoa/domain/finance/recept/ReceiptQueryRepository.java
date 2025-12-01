package com.coffemoa.domain.finance.recept;

import java.util.List;

public interface ReceiptQueryRepository {

  List<Receipt> searchByDateRange(ReceiptSearchRequest request);

}