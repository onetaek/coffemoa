package com.coffemoa.domain.finance.recept;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
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

  @Override
  public List<ReceiptDailyCountResponse> findDailyUploadCount(
      LocalDate startDate, LocalDate endDate
  ) {

    QReceipt receipt = QReceipt.receipt;

    return query
        .select(Projections.constructor(
            ReceiptDailyCountResponse.class,
            receipt.salesDate,
            receipt.id.count()
        ))
        .from(receipt)
        .where(
            receipt.salesDate.between(startDate, endDate),
            receipt.deletedAt.isNull()
        )
        .groupBy(receipt.salesDate)
        .orderBy(receipt.salesDate.asc())
        .fetch();
  }
}
