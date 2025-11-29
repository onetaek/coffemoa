package com.coffemoa.domain.standard.cafemenu;

import com.coffemoa.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cafe-menus")
@RequiredArgsConstructor
public class CafeMenuController {

  private final CafeMenuService cafeMenuService;

  @GetMapping
  public ApiResponse<List<CafeMenuResponse>> search(CafeMenuSearchRequest request) {
    return ApiResponse.ok(cafeMenuService.getList(request));
  }

  @PostMapping("/cud")
  public ApiResponse<Void> cud(@RequestBody List<CafeMenuCUDRequest> requests) {
    cafeMenuService.process(requests);
    return ApiResponse.ok();
  }
}
