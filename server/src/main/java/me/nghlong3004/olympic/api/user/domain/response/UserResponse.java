package me.nghlong3004.olympic.api.user.domain.response;

import java.util.List;
import java.util.UUID;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
public record UserResponse(UUID id, String email, String fullName, List<String> roles) {}
