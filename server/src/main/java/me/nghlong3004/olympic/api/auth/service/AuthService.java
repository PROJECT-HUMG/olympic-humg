package me.nghlong3004.olympic.api.auth.service;

import me.nghlong3004.olympic.api.auth.domain.request.LoginRequest;
import me.nghlong3004.olympic.api.auth.domain.request.RefreshTokenRequest;
import me.nghlong3004.olympic.api.auth.domain.request.RegisterRequest;
import me.nghlong3004.olympic.api.auth.domain.response.AuthResponse;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
public interface AuthService {

  AuthResponse register(RegisterRequest request);

  AuthResponse login(LoginRequest request);

  AuthResponse refresh(RefreshTokenRequest request);

  void logout(RefreshTokenRequest request, String userId);
}
