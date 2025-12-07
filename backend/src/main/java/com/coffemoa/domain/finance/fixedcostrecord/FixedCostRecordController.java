package com.coffemoa.domain.finance.fixedcostrecord;

import com.coffemoa.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fixed-cost-records")
@RequiredArgsConstructor
public class FixedCostRecordController {

  private final FixedCostRecordService service;

  @GetMapping
  public ApiResponse<List<FixedCostRecordResponse>> search(FixedCostRecordSearchRequest request) {
    return ApiResponse.ok(service.getList(request));
  }

  @PostMapping("/cud")
  public ApiResponse<Void> cud(@RequestBody List<FixedCostRecordCUDRequest> request) {
    service.process(request);
    return ApiResponse.ok();
  }
}
