package com.coffemoa.domain.auth.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
  private String token;          // JWT
  private String username;
  private String role;           // 대표 역할명 (필요시)
  private String roleId;         // 대표 역할 ID (문자열이면 변환)
  private Set<String> permissions; // example:dialog:add ...
}