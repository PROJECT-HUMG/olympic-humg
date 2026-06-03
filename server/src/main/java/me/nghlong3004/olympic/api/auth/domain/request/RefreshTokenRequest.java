package me.nghlong3004.olympic.api.auth.domain.request;

import jakarta.validation.constraints.NotBlank;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
public record RefreshTokenRequest(@NotBlank String refreshToken) {}
