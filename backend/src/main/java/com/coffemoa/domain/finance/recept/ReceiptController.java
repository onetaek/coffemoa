package com.coffemoa.domain.finance.recept;

import com.coffemoa.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

  @PostMapping("/upload")
  public ApiResponse<Void> upload(@RequestBody ReceiptUploadRequest request) {
    receiptService.uploadReceipt(request);
    return ApiResponse.ok();
  }
}