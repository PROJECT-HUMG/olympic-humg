package me.nghlong3004.olympic.api.user.service.impl;

import me.nghlong3004.olympic.api.common.exception.DuplicateResourceException;
import me.nghlong3004.olympic.api.common.exception.ResourceNotFoundException;
import me.nghlong3004.olympic.api.user.dto.CreateUserRequest;
import me.nghlong3004.olympic.api.user.dto.UpdateUserRequest;
import me.nghlong3004.olympic.api.user.dto.UserResponse;
import me.nghlong3004.olympic.api.user.entity.Role;
import me.nghlong3004.olympic.api.user.entity.User;
import me.nghlong3004.olympic.api.user.mapper.UserMapper;
import me.nghlong3004.olympic.api.user.repository.RoleRepository;
import me.nghlong3004.olympic.api.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for {@link UserServiceImpl}.
 *
 * @author nghlong3004
 * @since 2026-06-05
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl")
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserServiceImpl userService;

    private Role adminRole;
    private Role studentRole;
    private User testUser;
    private UUID userPublicId;
    private UUID rolePublicId;

    @BeforeEach
    void setUp() {
        rolePublicId = UUID.randomUUID();
        adminRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        studentRole = Role.builder()
                .id(2L)
                .name("STUDENT")
                .build();

        userPublicId = UUID.randomUUID();
        testUser = User.builder()
                .email("test@humg.edu.vn")
                .passwordHash("$2a$10$hashed")
                .fullName("Nguyen Van A")
                .role(studentRole)
                .enabled(true)
                .build();
        testUser.setId(1L);
        // BaseEntity fields are set via @Builder.Default, but publicId is random;
        // override for deterministic tests
        testUser.setPublicId(userPublicId);
    }

    @Nested
    @DisplayName("getByPublicId()")
    class GetByPublicId {

        @Test
        @DisplayName("should return user when found")
        void shouldReturnUser() {
            var expectedResponse = new UserResponse(
                    userPublicId, "test@humg.edu.vn", "Nguyen Van A", null, null, null,
                    "STUDENT", true, false, Instant.now()
            );
            given(userRepository.findByPublicId(userPublicId)).willReturn(Optional.of(testUser));
            given(userMapper.toResponse(testUser)).willReturn(expectedResponse);

            UserResponse result = userService.getByPublicId(userPublicId);

            assertThat(result).isNotNull();
            assertThat(result.email()).isEqualTo("test@humg.edu.vn");
            assertThat(result.role()).isEqualTo("STUDENT");
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when not found")
        void shouldThrowWhenNotFound() {
            UUID unknownId = UUID.randomUUID();
            given(userRepository.findByPublicId(unknownId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> userService.getByPublicId(unknownId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("User");
        }
    }

    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("should return paginated active users")
        void shouldReturnPaginatedUsers() {
            var pageable = PageRequest.of(0, 10);
            var usersPage = new PageImpl<>(List.of(testUser), pageable, 1);
            var response = new UserResponse(
                    userPublicId, "test@humg.edu.vn", "Nguyen Van A", null, null, null,
                    "STUDENT", true, false, Instant.now()
            );
            given(userRepository.findAllActive(pageable)).willReturn(usersPage);
            given(userMapper.toResponse(testUser)).willReturn(response);

            Page<UserResponse> result = userService.findAll(pageable);

            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().getFirst().email()).isEqualTo("test@humg.edu.vn");
        }
    }

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("should create user with valid request")
        void shouldCreateUser() {
            var request = new CreateUserRequest(
                    "new@humg.edu.vn", "password123", "Tran Van B",
                    "0901234567", null, rolePublicId
            );
            var expectedResponse = new UserResponse(
                    UUID.randomUUID(), "new@humg.edu.vn", "Tran Van B", "0901234567", null, null,
                    "STUDENT", true, false, Instant.now()
            );

            given(userRepository.existsByEmail("new@humg.edu.vn")).willReturn(false);
            given(roleRepository.findByPublicId(rolePublicId)).willReturn(Optional.of(studentRole));
            given(passwordEncoder.encode("password123")).willReturn("$2a$10$encoded");
            given(userRepository.save(any(User.class))).willAnswer(inv -> {
                User saved = inv.getArgument(0);
                saved.setId(2L);
                return saved;
            });
            given(userMapper.toResponse(any(User.class))).willReturn(expectedResponse);

            UserResponse result = userService.create(request);

            assertThat(result.email()).isEqualTo("new@humg.edu.vn");
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("should throw DuplicateResourceException when email exists")
        void shouldThrowWhenDuplicateEmail() {
            var request = new CreateUserRequest(
                    "existing@humg.edu.vn", "password123", "Tran Van C",
                    null, null, rolePublicId
            );
            given(userRepository.existsByEmail("existing@humg.edu.vn")).willReturn(true);

            assertThatThrownBy(() -> userService.create(request))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessageContaining("email");
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {

        @Test
        @DisplayName("should update user fields")
        void shouldUpdateUser() {
            var request = new UpdateUserRequest(
                    null, "Nguyen Van B Updated", "0909999999", null, null
            );
            var expectedResponse = new UserResponse(
                    userPublicId, "test@humg.edu.vn", "Nguyen Van B Updated", "0909999999", null, null,
                    "STUDENT", true, false, Instant.now()
            );

            given(userRepository.findByPublicId(userPublicId)).willReturn(Optional.of(testUser));
            given(userRepository.save(any(User.class))).willReturn(testUser);
            given(userMapper.toResponse(testUser)).willReturn(expectedResponse);

            UserResponse result = userService.update(userPublicId, request);

            assertThat(result.fullName()).isEqualTo("Nguyen Van B Updated");
        }

        @Test
        @DisplayName("should throw when updating email to duplicate")
        void shouldThrowWhenDuplicateEmail() {
            var request = new UpdateUserRequest(
                    "taken@humg.edu.vn", null, null, null, null
            );
            given(userRepository.findByPublicId(userPublicId)).willReturn(Optional.of(testUser));
            given(userRepository.existsByEmail("taken@humg.edu.vn")).willReturn(true);

            assertThatThrownBy(() -> userService.update(userPublicId, request))
                    .isInstanceOf(DuplicateResourceException.class);
        }
    }

    @Nested
    @DisplayName("changeRole()")
    class ChangeRole {

        @Test
        @DisplayName("should change user role from STUDENT to ADMIN")
        void shouldChangeRole() {
            UUID adminRolePublicId = UUID.randomUUID();
            adminRole.setPublicId(adminRolePublicId);

            given(userRepository.findByPublicId(userPublicId)).willReturn(Optional.of(testUser));
            given(roleRepository.findByPublicId(adminRolePublicId)).willReturn(Optional.of(adminRole));

            userService.changeRole(userPublicId, adminRolePublicId);

            assertThat(testUser.getRole().getName()).isEqualTo("ADMIN");
            verify(userRepository).save(testUser);
        }
    }

    @Nested
    @DisplayName("toggleStatus()")
    class ToggleStatus {

        @Test
        @DisplayName("should toggle enabled to disabled")
        void shouldToggle() {
            given(userRepository.findByPublicId(userPublicId)).willReturn(Optional.of(testUser));

            assertThat(testUser.isEnabled()).isTrue();

            userService.toggleStatus(userPublicId);

            assertThat(testUser.isEnabled()).isFalse();
            verify(userRepository).save(testUser);
        }
    }

    @Nested
    @DisplayName("softDelete()")
    class SoftDelete {

        @Test
        @DisplayName("should soft-delete user")
        void shouldSoftDelete() {
            given(userRepository.findByPublicId(userPublicId)).willReturn(Optional.of(testUser));

            userService.softDelete(userPublicId, 99L);

            assertThat(testUser.isDeleted()).isTrue();
            assertThat(testUser.getDeletedBy()).isEqualTo(99L);
            verify(userRepository).save(testUser);
        }

        @Test
        @DisplayName("should throw when user not found")
        void shouldThrowWhenNotFound() {
            UUID unknownId = UUID.randomUUID();
            given(userRepository.findByPublicId(unknownId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> userService.softDelete(unknownId, 99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
