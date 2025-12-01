package com.coffemoa.domain.finance.cafemenucost;

import com.coffemoa.global.dto.ApiResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe-menu-costs")
public class CafeMenuCostController {

  private final CafeMenuCostService cafeMenuCostService;

  @GetMapping
  public ApiResponse<List<CafeMenuCostResponse>> select(@RequestParam Map<String, String> param) {
    return ApiResponse.ok(cafeMenuCostService.getMenuCostList());
  }

}
