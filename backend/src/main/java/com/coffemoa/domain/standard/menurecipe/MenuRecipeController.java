package com.coffemoa.domain.standard.menurecipe;

import com.coffemoa.global.dto.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/menu-recipes")
@RequiredArgsConstructor
public class MenuRecipeController {

  private final MenuRecipeService menuRecipeService;

  @GetMapping
  public ApiResponse<List<MenuRecipeResponse>> search(MenuRecipeSearchRequest request) {
    return ApiResponse.ok(menuRecipeService.getList(request));
  }

  @PostMapping("/cud")
  public ApiResponse<Void> cud(@RequestBody List<MenuRecipeCUDRequest> requests) {
    menuRecipeService.process(requests);
    return ApiResponse.ok();
  }
}
