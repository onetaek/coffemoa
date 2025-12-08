package com.coffemoa.domain.finance.recept;

import com.coffemoa.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/receipt")
@RequiredArgsConstructor
public class ReceiptController {

  private final ReceiptService receiptService;

  @GetMapping
  public ApiResponse<List<ReceiptResponse>> search(ReceiptSearchRequest request) {
    return ApiResponse.ok(receiptService.search(request));
  }

  @GetMapping("cost")
  public ApiResponse<ReceiptCostResponse> searchCost(ReceiptSearchRequest request) {
    return ApiResponse.ok(receiptService.searchCost(request));
  }

  @GetMapping("/uploaded-summary")
  public ApiResponse<List<ReceiptDailyCountResponse>> getMonthlySummary(
      @RequestParam int year,
      @RequestParam int month
  ) {
    return ApiResponse.ok(receiptService.getMonthlyUploadSummary(year, month));
  }

  @PutMapping("/upload")
  public ApiResponse<Void> upload(@RequestBody ReceiptUploadRequest request) {
    receiptService.uploadReceipt(request);
    return ApiResponse.ok();
  }
}