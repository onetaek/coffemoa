package com.coffemoa.domain.standard.unit;

import com.coffemoa.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {

  private final UnitService unitService;

  @GetMapping
  public ApiResponse<List<UnitResponse>> search(UnitSearchRequest request) {
    return ApiResponse.ok(unitService.getList(request));
  }

  @PostMapping("/cud")
  public ApiResponse<Void> cud(@RequestBody List<UnitCUDRequest> requests) {
    unitService.process(requests);
    return ApiResponse.ok();
  }
}
