package com.coffemoa.domain.standard.controller;

import com.coffemoa.domain.standard.entity.CafeMenu;
import com.coffemoa.domain.standard.mapper.CafeMenuMapper;
import com.coffemoa.global.dto.ApiResponse;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cafe-menus")
public class CafeMenuController {

  private final CafeMenuMapper mapper;

  @PostMapping
  public ApiResponse<Void> insert(@RequestBody Map<String, String> param) {
    mapper.insert(param);
    return ApiResponse.ok(null);
  }

  @GetMapping
  public ApiResponse<List<CafeMenu>> select(@RequestParam Map<String, String> param) {
    return ApiResponse.ok(mapper.select(param));
  }

  @PutMapping
  public ApiResponse<Void> update(@RequestBody Map<String, String> param) {
    mapper.update(param);
    return ApiResponse.ok(null);
  }

  @DeleteMapping
  public ApiResponse<Void> delete(@RequestParam Map<String, String> param) {
    mapper.delete(param);
    return ApiResponse.ok(null);
  }
}
