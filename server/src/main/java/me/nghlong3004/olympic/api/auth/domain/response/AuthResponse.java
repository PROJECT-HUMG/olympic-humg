package me.nghlong3004.olympic.api.auth.domain.response;

import me.nghlong3004.olympic.api.user.domain.response.UserResponse;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
public record AuthResponse(
    String accessToken,
    String refreshToken,
    String tokenType,
    long expiresIn,
    UserResponse user) {}
