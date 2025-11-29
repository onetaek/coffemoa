package com.coffemoa.domain.standard.unitconversion;

import com.coffemoa.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/unit-conversions")
@RequiredArgsConstructor
public class UnitConversionController {

  private final UnitConversionService unitConversionService;

  @GetMapping
  public ApiResponse<List<UnitConversionResponse>> search(UnitConversionSearchRequest request) {
    return ApiResponse.ok(unitConversionService.getList(request));
  }

  @PostMapping("/cud")
  public ApiResponse<Void> cud(@RequestBody List<UnitConversionCUDRequest> requests) {
    unitConversionService.process(requests);
    return ApiResponse.ok();
  }
}
