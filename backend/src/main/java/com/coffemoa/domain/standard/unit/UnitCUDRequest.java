package com.coffemoa.domain.standard.unit;

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
public class UnitCUDRequest {

  private Long id;
  private String name;
  private String description;
  private CUDFlag flag;
}
