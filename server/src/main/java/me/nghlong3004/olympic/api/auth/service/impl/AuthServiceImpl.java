package me.nghlong3004.olympic.api.auth.service.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import me.nghlong3004.olympic.api.auth.domain.RefreshToken;
import me.nghlong3004.olympic.api.auth.domain.request.LoginRequest;
import me.nghlong3004.olympic.api.auth.domain.request.RefreshTokenRequest;
import me.nghlong3004.olympic.api.auth.domain.request.RegisterRequest;
import me.nghlong3004.olympic.api.auth.domain.response.AuthResponse;
import me.nghlong3004.olympic.api.auth.mapper.AuthMapper;
import me.nghlong3004.olympic.api.auth.repository.RefreshTokenRepository;
import me.nghlong3004.olympic.api.auth.service.AuthService;
import me.nghlong3004.olympic.api.auth.service.TokenService;
import me.nghlong3004.olympic.api.common.exception.ErrorCode;
import me.nghlong3004.olympic.api.common.exception.ResourceException;
import me.nghlong3004.olympic.api.user.domain.Role;
import me.nghlong3004.olympic.api.user.domain.User;
import me.nghlong3004.olympic.api.user.repository.RoleRepository;
import me.nghlong3004.olympic.api.user.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "olympic.auth.enabled", havingValue = "true", matchIfMissing = true)
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

  private static final String DEFAULT_REGISTER_ROLE = "STUDENT";

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final TokenService tokenService;
  private final AuthMapper authMapper;

  @Override
  @Transactional
  public AuthResponse register(RegisterRequest request) {
    String email = normalizeEmail(request.email());
    if (userRepository.existsByEmailIgnoreCase(email)) {
      throw new ResourceException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    Role studentRole =
        roleRepository
            .findByCode(DEFAULT_REGISTER_ROLE)
            .orElseThrow(() -> new ResourceException(ErrorCode.RESOURCE_NOT_FOUND));

    User user = new User();
    user.setEmail(email);
    user.setFullName(request.fullName().trim());
    user.setPasswordHash(passwordEncoder.encode(request.password()));
    user.getRoles().add(studentRole);

    return issueTokens(userRepository.save(user));
  }

  @Override
  @Transactional
  public AuthResponse login(LoginRequest request) {
    String email = normalizeEmail(request.email());
    authenticate(email, request.password());
    User user =
        userRepository
            .findByEmailIgnoreCase(email)
            .orElseThrow(() -> new ResourceException(ErrorCode.BAD_CREDENTIALS));
    return issueTokens(user);
  }

  @Override
  @Transactional
  public AuthResponse refresh(RefreshTokenRequest request) {
    RefreshToken refreshToken = findActiveRefreshToken(request.refreshToken());
    Instant now = Instant.now();
    if (refreshToken.getExpiresAt().isBefore(now)) {
      refreshToken.setRevokedAt(now);
      throw new ResourceException(ErrorCode.REFRESH_TOKEN_EXPIRED);
    }

    refreshToken.setRevokedAt(now);
    return issueTokens(refreshToken.getUser());
  }

  @Override
  @Transactional
  public void logout(RefreshTokenRequest request, String userId) {
    RefreshToken refreshToken = findActiveRefreshToken(request.refreshToken());
    if (!refreshToken.getUser().getId().equals(UUID.fromString(userId))) {
      throw new ResourceException(ErrorCode.ACCESS_DENIED);
    }
    refreshToken.setRevokedAt(Instant.now());
  }

  private AuthResponse issueTokens(User user) {
    String accessToken = tokenService.createAccessToken(user);
    String refreshTokenValue = tokenService.createRefreshToken();

    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(user);
    refreshToken.setTokenHash(hash(refreshTokenValue));
    refreshToken.setExpiresAt(Instant.now().plus(tokenService.refreshTokenTtl()));
    refreshTokenRepository.save(refreshToken);

    return authMapper.toResponse(
        user, accessToken, refreshTokenValue, tokenService.accessExpiresInSeconds());
  }

  private RefreshToken findActiveRefreshToken(String token) {
    return refreshTokenRepository
        .findByTokenHashAndRevokedAtIsNull(hash(token))
        .orElseThrow(() -> new ResourceException(ErrorCode.INVALID_REFRESH_TOKEN));
  }

  private void authenticate(String email, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    } catch (DisabledException e) {
      throw new ResourceException(ErrorCode.ACCOUNT_LOCKED);
    } catch (BadCredentialsException e) {
      throw new ResourceException(ErrorCode.BAD_CREDENTIALS);
    } catch (AuthenticationException e) {
      throw new ResourceException(ErrorCode.BAD_CREDENTIALS);
    }
  }

  private String normalizeEmail(String email) {
    return email.trim().toLowerCase(Locale.ROOT);
  }

  private String hash(String value) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashed = digest.digest(value.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hashed);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("SHA-256 is not available.", e);
    }
  }
}
