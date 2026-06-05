package me.nghlong3004.olympic.api.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import me.nghlong3004.olympic.api.common.exception.GlobalExceptionHandler;
import me.nghlong3004.olympic.api.common.exception.ResourceNotFoundException;
import me.nghlong3004.olympic.api.config.TestSecurityConfig;
import me.nghlong3004.olympic.api.user.dto.CreateUserRequest;
import me.nghlong3004.olympic.api.user.dto.UpdateUserRequest;
import me.nghlong3004.olympic.api.user.dto.UserResponse;
import me.nghlong3004.olympic.api.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Slice tests for {@link UserController}.
 *
 * @author nghlong3004
 * @since 2026-06-05
 */
@WebMvcTest(UserController.class)
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
@DisplayName("UserController")
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    @MockitoBean private UserService userService;

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID ROLE_ID = UUID.randomUUID();
    private static final UserResponse SAMPLE = new UserResponse(
            USER_ID, "user@humg.edu.vn", "Nguyễn Văn A", "0901234567",
            null, "DH2024001", "STUDENT", true, false, Instant.now()
    );

    @Nested
    @DisplayName("GET /api/v1/users/me")
    class GetMe {

        @Test
        @DisplayName("should return current user profile")
        void shouldReturnCurrentUser() throws Exception {
            given(userService.getByPublicId(USER_ID)).willReturn(SAMPLE);

            mockMvc.perform(get("/api/v1/users/me")
                            .with(jwt().jwt(j -> j.subject(USER_ID.toString()))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email").value("user@humg.edu.vn"));
        }

        @Test
        @DisplayName("should reject unauthenticated")
        void shouldRejectUnauthenticated() throws Exception {
            mockMvc.perform(get("/api/v1/users/me"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/users")
    class ListUsers {

        @Test
        @DisplayName("should list users for admin")
        void shouldListForAdmin() throws Exception {
            given(userService.findAll(any(Pageable.class)))
                    .willReturn(new PageImpl<>(List.of(SAMPLE)));

            mockMvc.perform(get("/api/v1/users")
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].email").value("user@humg.edu.vn"));
        }

        @Test
        @DisplayName("should filter by role")
        void shouldFilterByRole() throws Exception {
            given(userService.findByRole(eq("STUDENT"), any(Pageable.class)))
                    .willReturn(new PageImpl<>(List.of(SAMPLE)));

            mockMvc.perform(get("/api/v1/users").param("role", "STUDENT")
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("should return 403 for non-admin")
        void shouldRejectNonAdmin() throws Exception {
            mockMvc.perform(get("/api/v1/users")
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_STUDENT"))))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/users/{id}")
    class GetById {

        @Test
        @DisplayName("should return user by ID for admin")
        void shouldReturnUser() throws Exception {
            given(userService.getByPublicId(USER_ID)).willReturn(SAMPLE);

            mockMvc.perform(get("/api/v1/users/{id}", USER_ID)
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.fullName").value("Nguyễn Văn A"));
        }

        @Test
        @DisplayName("should return 404 when not found")
        void shouldReturn404() throws Exception {
            UUID unknownId = UUID.randomUUID();
            given(userService.getByPublicId(unknownId))
                    .willThrow(new ResourceNotFoundException("User", unknownId));

            mockMvc.perform(get("/api/v1/users/{id}", unknownId)
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/users")
    class CreateUser {

        @Test
        @DisplayName("should create user for admin")
        void shouldCreateForAdmin() throws Exception {
            var request = new CreateUserRequest("new@mail.com", "Password1", "New User", null, null, ROLE_ID);
            given(userService.create(any(CreateUserRequest.class))).willReturn(SAMPLE);

            mockMvc.perform(post("/api/v1/users")
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("should return 400 for invalid request")
        void shouldRejectInvalidRequest() throws Exception {
            var request = new CreateUserRequest("", "", "", null, null, null);
            mockMvc.perform(post("/api/v1/users")
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 403 for non-admin")
        void shouldRejectNonAdmin() throws Exception {
            var request = new CreateUserRequest("ok@mail.com", "Password1", "Name", null, null, ROLE_ID);
            mockMvc.perform(post("/api/v1/users")
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_STUDENT")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/users/{id}")
    class UpdateUser {

        @Test
        @DisplayName("should update user for admin")
        void shouldUpdateForAdmin() throws Exception {
            var request = new UpdateUserRequest("new@mail.com", "Updated Name", null, null, null);
            given(userService.update(eq(USER_ID), any(UpdateUserRequest.class))).willReturn(SAMPLE);

            mockMvc.perform(put("/api/v1/users/{id}", USER_ID)
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/users/{id}/change-role")
    class ChangeRole {

        @Test
        @DisplayName("should change role for admin")
        void shouldChangeRoleForAdmin() throws Exception {
            doNothing().when(userService).changeRole(USER_ID, ROLE_ID);

            mockMvc.perform(patch("/api/v1/users/{id}/change-role", USER_ID)
                            .param("roleId", ROLE_ID.toString())
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("should return 403 for non-admin")
        void shouldRejectNonAdmin() throws Exception {
            mockMvc.perform(patch("/api/v1/users/{id}/change-role", USER_ID)
                            .param("roleId", ROLE_ID.toString())
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_STUDENT"))))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/users/{id}/toggle-status")
    class ToggleStatus {

        @Test
        @DisplayName("should toggle status for admin")
        void shouldToggleForAdmin() throws Exception {
            doNothing().when(userService).toggleStatus(USER_ID);

            mockMvc.perform(post("/api/v1/users/{id}/toggle-status", USER_ID)
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                    .andExpect(status().isNoContent());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/users/{id}")
    class DeleteUser {

        @Test
        @DisplayName("should soft-delete user for admin")
        void shouldDeleteForAdmin() throws Exception {
            doNothing().when(userService).softDelete(eq(USER_ID), any());

            mockMvc.perform(delete("/api/v1/users/{id}", USER_ID)
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("should return 404 when user not found")
        void shouldReturn404() throws Exception {
            UUID unknownId = UUID.randomUUID();
            doThrow(new ResourceNotFoundException("User", unknownId))
                    .when(userService).softDelete(eq(unknownId), any());

            mockMvc.perform(delete("/api/v1/users/{id}", unknownId)
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("should return 403 for non-admin")
        void shouldRejectNonAdmin() throws Exception {
            mockMvc.perform(delete("/api/v1/users/{id}", USER_ID)
                            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_STUDENT"))))
                    .andExpect(status().isForbidden());
        }
    }
}
