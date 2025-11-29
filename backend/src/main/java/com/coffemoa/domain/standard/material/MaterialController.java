package com.coffemoa.domain.standard.material;

import com.coffemoa.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

  private final MaterialService materialService;

  @GetMapping
  public ApiResponse<List<MaterialResponse>> search(MaterialSearchRequest request) {
    return ApiResponse.ok(materialService.getList(request));
  }

  @PostMapping("/cud")
  public ApiResponse<Void> cud(@RequestBody List<MaterialCUDRequest> requests) {
    materialService.process(requests);
    return ApiResponse.ok();
  }
}
