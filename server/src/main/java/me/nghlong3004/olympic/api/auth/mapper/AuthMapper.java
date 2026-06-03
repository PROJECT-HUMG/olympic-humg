package me.nghlong3004.olympic.api.auth.mapper;

import me.nghlong3004.olympic.api.auth.domain.response.AuthResponse;
import me.nghlong3004.olympic.api.user.domain.User;
import me.nghlong3004.olympic.api.user.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
@Mapper(componentModel = "spring")
public abstract class AuthMapper {

  @Autowired protected UserMapper userMapper;

  public AuthResponse toResponse(
      User user, String accessToken, String refreshToken, long expiresInSeconds) {
    return new AuthResponse(
        accessToken, refreshToken, "Bearer", expiresInSeconds, userMapper.toResponse(user));
  }
}
