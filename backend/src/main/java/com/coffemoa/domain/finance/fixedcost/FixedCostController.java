package com.coffemoa.domain.finance.fixedcost;

import com.coffemoa.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fixed-costs")
@RequiredArgsConstructor
public class FixedCostController {

    private final FixedCostService service;

    @GetMapping
    public ApiResponse<List<FixedCostResponse>> search(FixedCostSearchRequest request) {
        return ApiResponse.ok(service.getList(request));
    }

    @PostMapping("/cud")
    public ApiResponse<Void> cud(@RequestBody List<FixedCostCUDRequest> request) {
        service.process(request);
        return ApiResponse.ok();
    }
}
