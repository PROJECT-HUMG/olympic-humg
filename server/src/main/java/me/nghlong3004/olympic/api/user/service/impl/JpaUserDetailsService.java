package me.nghlong3004.olympic.api.user.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.nghlong3004.olympic.api.user.domain.Permission;
import me.nghlong3004.olympic.api.user.domain.Role;
import me.nghlong3004.olympic.api.user.domain.User;
import me.nghlong3004.olympic.api.user.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
@Service
@Primary
@RequiredArgsConstructor
@ConditionalOnProperty(name = "olympic.auth.enabled", havingValue = "true", matchIfMissing = true)
public class JpaUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByEmailIgnoreCase(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found."));

    return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
        .password(user.getPasswordHash())
        .disabled(User.STATUS_DISABLED.equals(user.getStatus()))
        .authorities(authorities(user))
        .build();
  }

  private List<SimpleGrantedAuthority> authorities(User user) {
    return user.getRoles().stream()
        .flatMap(
            role ->
                java.util.stream.Stream.concat(
                    java.util.stream.Stream.of(role.getCode()),
                    role.getPermissions().stream().map(Permission::getCode)))
        .distinct()
        .sorted()
        .map(SimpleGrantedAuthority::new)
        .toList();
  }
}
