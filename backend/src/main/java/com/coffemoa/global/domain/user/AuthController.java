package com.coffemoa.global.domain.user;

import com.coffemoa.global.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
    return ApiResponse.ok(authService.login(request));
  }

  @PostMapping("/signup")
  public ApiResponse<SignupResponse> signup(@RequestBody SignupRequest request) {
    return ApiResponse.ok(authService.signup(request));
  }
}