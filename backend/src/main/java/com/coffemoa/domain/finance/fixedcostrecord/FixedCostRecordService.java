package com.coffemoa.domain.finance.fixedcostrecord;

import com.coffemoa.domain.finance.fixedcost.FixedCost;
import com.coffemoa.domain.finance.fixedcost.FixedCostRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FixedCostRecordService {

  private final FixedCostRecordRepository recordRepository;
  private final FixedCostRepository fixedCostRepository;

  @Transactional(readOnly = true)
  public List<FixedCostRecordResponse> getList(FixedCostRecordSearchRequest request) {

    return recordRepository.search(request).stream()
        .map(FixedCostRecordResponse::fromEntity)
        .toList();
  }

  public void process(List<FixedCostRecordCUDRequest> list) {

    for (FixedCostRecordCUDRequest req : list) {
      switch (req.getFlag()) {
        case C -> create(req);
        case U -> update(req);
        case D -> delete(req);
      }
    }
  }

  private void create(FixedCostRecordCUDRequest req) {

    FixedCost fc = fixedCostRepository.findById(req.getFixedCostId())
        .orElseThrow(() -> new RuntimeException("not found"));

    FixedCostRecord rec = FixedCostRecord.builder()
        .fixedCost(fc)
        .periodValue(req.getPeriodValue())  // String
        .amount(req.getAmount())
        .build();

    recordRepository.save(rec);
  }

  private void update(FixedCostRecordCUDRequest req) {

    FixedCostRecord rec = recordRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("Record not found"));

    rec.setPeriodValue(req.getPeriodValue());
    rec.setAmount(req.getAmount());
  }


  private void delete(FixedCostRecordCUDRequest req) {

    FixedCostRecord record = recordRepository.findById(req.getId())
        .orElseThrow(() -> new RuntimeException("FixedCostRecord not found"));

    record.markDeleted();
  }

  public List<FixedCostRecord> getApplicableFixedCosts(LocalDate fromDate, LocalDate toDate) {
    return recordRepository.findAll().stream()
        .filter(rec -> isFixedCostInRange(rec, fromDate, toDate))
        .toList();
  }

  private boolean isFixedCostInRange(FixedCostRecord rec, LocalDate from, LocalDate to) {

    String type = rec.getFixedCost().getPeriodType().getCode();
    String value = rec.getPeriodValue();

    if (type.equals("YEARLY")) {
      int year = Integer.parseInt(value);
      return year >= from.getYear() && year <= to.getYear();
    }

    if (type.equals("MONTHLY")) {
      String ym = value; // "2025-12"
      LocalDate monthDate = LocalDate.parse(ym + "-01");
      return !monthDate.isBefore(from.withDayOfMonth(1)) &&
          !monthDate.isAfter(to.withDayOfMonth(1));
    }

    if (type.equals("DAILY")) {
      LocalDate date = LocalDate.parse(value);
      return !date.isBefore(from) && !date.isAfter(to);
    }

    return false;
  }

}
