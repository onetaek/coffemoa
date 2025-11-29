package com.coffemoa.domain.standard.cafemenu;

import java.time.LocalDateTime;
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
public class CafeMenuResponse {

  private Long id;
  private String name;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static CafeMenuResponse fromEntity(CafeMenu cafeMenu) {
    return CafeMenuResponse.builder()
        .id(cafeMenu.getId())
        .name(cafeMenu.getName())
        .createdAt(cafeMenu.getCreatedAt())
        .updatedAt(cafeMenu.getUpdatedAt())
        .build();
  }
}
