package com.coffemoa.domain.auth.service;

import com.coffemoa.domain.auth.AuthConstants;
import com.coffemoa.domain.auth.dto.LoginRequest;
import com.coffemoa.domain.auth.dto.LoginResponse;
import com.coffemoa.domain.auth.dto.SignupRequest;
import com.coffemoa.domain.auth.dto.SignupResponse;
import com.coffemoa.domain.auth.entity.Role;
import com.coffemoa.domain.auth.entity.User;
import com.coffemoa.domain.auth.entity.UserRole;
import com.coffemoa.domain.auth.jwt.JwtTokenProvider;
import com.coffemoa.domain.auth.repository.RoleQueryRepository;
import com.coffemoa.domain.auth.repository.RoleRepository;
import com.coffemoa.domain.auth.repository.UserQueryRepository;
import com.coffemoa.domain.auth.repository.UserRepository;
import com.coffemoa.domain.auth.repository.UserRoleRepository;
import com.coffemoa.global.BusinessException;
import com.coffemoa.global.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  private final UserQueryRepository userQueryRepository;
  private final UserRoleRepository userRoleRepository;
  private final RoleQueryRepository roleQueryRepository;

  @Transactional
  public SignupResponse signup(SignupRequest req) {
    if (userQueryRepository.existsByUsername(req.getUsername())) {
      throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
    }

    // 1) 사용자 생성
    User user = userRepository.save(
        User.builder()
            .username(req.getUsername())
            .password(passwordEncoder.encode(req.getPassword()))
            .email(req.getEmail())
            .status(1)
            .build()
    );

    // 2) 기본 역할 확보: 없으면 생성
    Role basic = roleQueryRepository.findByCode(AuthConstants.DEFAULT_ROLE_NAME)
        .orElseGet(() -> {
          try {
            return roleRepository.save(
                Role.builder()
                    .code(AuthConstants.DEFAULT_ROLE_NAME)
                    .name("기본권한")
                    .status(1)
                    .remark("Auto created default role for signup")
                    .build()
            );
          } catch (DataIntegrityViolationException e) {
            // 동시성으로 이미 누군가 만들었다면 재조회
            return roleQueryRepository.findByCode(AuthConstants.DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new BusinessException(ErrorCode.DEFAULT_ROLE_NOT_FOUND));
          }
        });

    // 3) 사용자-역할 매핑
    userRoleRepository.save(
        UserRole.builder()
            .user(user)
            .role(basic)
            .build()
    );

    // 4) 응답
    return SignupResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .build();
  }

  @Transactional
  public LoginResponse login(LoginRequest req) {
    User user = userRepository.findByUsername(req.getUsername())
        .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_LOGIN));

    if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
      throw new BusinessException(ErrorCode.INVALID_LOGIN);
    }

    String token = jwtTokenProvider.createToken(user.getUsername());

//    Set<String> permissions = userQueryRepository
//        .findAuthorityStringsByUserId(user.getId()); // 비어있으면 빈 Set

//    String primaryRoleName = user.getUserRoles().stream()
//        .findFirst().map(ur -> ur.getRole().getCode()).orElse(null);

//    String primaryRoleId = user.getUserRoles().stream()
//        .findFirst().map(ur -> String.valueOf(ur.getRole().getId())).orElse(null);

    return LoginResponse.builder()
        .token(token)
        .username(user.getUsername())
//        .role(primaryRoleName)
//        .roleId(primaryRoleId)
//        .permissions(permissions)
        .build();
  }
}