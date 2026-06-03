package me.nghlong3004.olympic.api.user.repository;

import java.util.Optional;
import java.util.UUID;
import me.nghlong3004.olympic.api.user.domain.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

  @EntityGraph(attributePaths = "permissions")
  Optional<Role> findByCode(String code);
}
