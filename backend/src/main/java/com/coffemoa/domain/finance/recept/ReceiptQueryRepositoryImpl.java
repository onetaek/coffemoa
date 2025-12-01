package com.coffemoa.domain.finance.recept;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReceiptQueryRepositoryImpl implements ReceiptQueryRepository {

  private final JPAQueryFactory query;

  @Override
  public List<Receipt> searchByDateRange(ReceiptSearchRequest request) {

    QReceipt receipt = QReceipt.receipt;

    return query
        .select(receipt)
        .from(receipt)
        .where(
            receipt.deletedAt.isNull(),
            receipt.salesDate.between(request.getFromDate(), request.getToDate())
        )
        .orderBy(
            receipt.salesDate.asc(),
            receipt.receiptNumber.asc(),
            receipt.productCode.asc()
        )
        .fetch();
  }
}
