package me.nghlong3004.olympic.api.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.nghlong3004.olympic.api.auth.domain.request.LoginRequest;
import me.nghlong3004.olympic.api.auth.domain.request.RefreshTokenRequest;
import me.nghlong3004.olympic.api.auth.domain.request.RegisterRequest;
import me.nghlong3004.olympic.api.auth.domain.response.AuthResponse;
import me.nghlong3004.olympic.api.auth.service.AuthService;
import me.nghlong3004.olympic.api.common.response.ApiResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(name = "olympic.auth.enabled", havingValue = "true", matchIfMissing = true)
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of(authService.register(request)));
  }

  @PostMapping("/login")
  public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
    return ApiResponse.of(authService.login(request));
  }

  @PostMapping("/refresh")
  public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
    return ApiResponse.of(authService.refresh(request));
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(
      @AuthenticationPrincipal Jwt jwt, @Valid @RequestBody RefreshTokenRequest request) {
    authService.logout(request, jwt.getSubject());
    return ResponseEntity.noContent().build();
  }
}
