package com.coffemoa.domain.standard.cafemenuprice;

import com.coffemoa.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cafe-menu-prices")
@RequiredArgsConstructor
public class CafeMenuPriceController {

  private final CafeMenuPriceService cafeMenuPriceService;

  @GetMapping
  public ApiResponse<List<CafeMenuPriceResponse>> search(CafeMenuPriceSearchRequest request) {
    return ApiResponse.ok(cafeMenuPriceService.getList(request));
  }

  @PostMapping("/cud")
  public ApiResponse<Void> cud(@RequestBody List<CafeMenuPriceCUDRequest> requests) {
    cafeMenuPriceService.process(requests);
    return ApiResponse.ok();
  }
}
