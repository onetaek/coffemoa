package com.coffemoa.domain.standard.cafemenu;

import com.coffemoa.global.CUDFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeMenuCUDRequest {

  private Long id;
  private String name;
  private CUDFlag flag; // 공통 CUDFlag 사용
}
