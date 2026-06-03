package me.nghlong3004.olympic.api.auth.repository;

import java.util.Optional;
import java.util.UUID;
import me.nghlong3004.olympic.api.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

  @EntityGraph(attributePaths = {"user", "user.roles", "user.roles.permissions"})
  Optional<RefreshToken> findByTokenHashAndRevokedAtIsNull(String tokenHash);
}
