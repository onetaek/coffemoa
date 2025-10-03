package com.coffemoa.domain.auth.controller;

import com.coffemoa.domain.auth.dto.LoginRequest;
import com.coffemoa.domain.auth.dto.SignupRequest;
import com.coffemoa.domain.auth.entity.User;
import com.coffemoa.domain.auth.jwt.JwtTokenProvider;
import com.coffemoa.domain.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final JwtTokenProvider jwtTokenProvider;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestBody LoginRequest request
  ) {
    var user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Invalid username or password");
    }

    String token = jwtTokenProvider.createToken(request.getUsername());
    return ResponseEntity.ok().body(token);
  }

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
    User user = User.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword())) // 🔐 암호화
        .email(request.getEmail())
        .build();

    userRepository.save(user);
    return ResponseEntity.ok("회원가입 성공");
  }
}