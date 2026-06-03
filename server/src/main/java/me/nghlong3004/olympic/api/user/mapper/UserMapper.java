package me.nghlong3004.olympic.api.user.mapper;

import java.util.Comparator;
import me.nghlong3004.olympic.api.user.domain.Role;
import me.nghlong3004.olympic.api.user.domain.User;
import me.nghlong3004.olympic.api.user.domain.response.UserResponse;
import org.mapstruct.Mapper;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

  default UserResponse toResponse(User user) {
    return new UserResponse(
        user.getId(),
        user.getEmail(),
        user.getFullName(),
        user.getRoles().stream().map(Role::getCode).sorted(Comparator.naturalOrder()).toList());
  }
}
