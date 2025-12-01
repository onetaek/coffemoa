package com.coffemoa.domain.finance.recept;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long>, ReceiptQueryRepository {

  List<Receipt> findAllBySalesDate(LocalDate salesDate);

}