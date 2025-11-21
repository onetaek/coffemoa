package com.coffemoa.domain.finance.controller;

import com.coffemoa.domain.finance.entity.CafeMenuCost;
import com.coffemoa.domain.finance.mapper.CafeMenuCostMapper;
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

  private final CafeMenuCostMapper mapper;

  @GetMapping
  public ApiResponse<List<CafeMenuCost>> select(@RequestParam Map<String, String> param) {
    return ApiResponse.ok(mapper.select(param));
  }

}
