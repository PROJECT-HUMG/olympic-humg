package me.nghlong3004.olympic.api.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.Cookie;
import me.nghlong3004.olympic.api.auth.dto.AuthResponse;
import me.nghlong3004.olympic.api.auth.dto.AuthResult;
import me.nghlong3004.olympic.api.auth.dto.LoginRequest;
import me.nghlong3004.olympic.api.auth.dto.RegisterRequest;
import me.nghlong3004.olympic.api.auth.service.AuthService;
import me.nghlong3004.olympic.api.common.exception.BusinessRuleException;
import me.nghlong3004.olympic.api.common.exception.GlobalExceptionHandler;
import me.nghlong3004.olympic.api.config.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Slice tests for {@link AuthController}.
 *
 * <p>Tests cover register, login, refresh, logout flows including
 * HttpOnly cookie handling and validation errors.
 *
 * @author nghlong3004
 * @since 2026-06-05
 */
@WebMvcTest(AuthController.class)
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
@DisplayName("AuthController")
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    @MockitoBean private AuthService authService;

    private static final Instant EXPIRY = Instant.now().plusSeconds(3600);
    private static final AuthResult SAMPLE_RESULT = new AuthResult(
            AuthResponse.bearer("access.jwt.token", EXPIRY),
            "refresh-token-value",
            EXPIRY
    );

    @Nested
    @DisplayName("POST /api/v1/auth/register")
    class Register {

        @Test
        @DisplayName("should register and return 201 with Set-Cookie header")
        void shouldRegisterSuccessfully() throws Exception {
            var request = new RegisterRequest("test@humg.edu.vn", "Password1", "Test User", null, null);
            given(authService.register(any(RegisterRequest.class))).willReturn(SAMPLE_RESULT);

            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.accessToken").value("access.jwt.token"))
                    .andExpect(jsonPath("$.tokenType").value("Bearer"))
                    .andExpect(header().exists("Set-Cookie"));
        }

        @Test
        @DisplayName("should return 400 for missing email")
        void shouldRejectMissingEmail() throws Exception {
            var request = new RegisterRequest("", "Password1", "Test", null, null);
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 for invalid email format")
        void shouldRejectInvalidEmail() throws Exception {
            var request = new RegisterRequest("not-email", "Password1", "Test", null, null);
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 for weak password")
        void shouldRejectWeakPassword() throws Exception {
            var request = new RegisterRequest("ok@mail.com", "short", "Test User", null, null);
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 400 for missing full name")
        void shouldRejectMissingName() throws Exception {
            var request = new RegisterRequest("ok@mail.com", "Password1", "", null, null);
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/auth/login")
    class Login {

        @Test
        @DisplayName("should login and return 200 with cookie")
        void shouldLoginSuccessfully() throws Exception {
            var request = new LoginRequest("user@humg.edu.vn", "Password1");
            given(authService.login(any(LoginRequest.class))).willReturn(SAMPLE_RESULT);

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(header().exists("Set-Cookie"));
        }

        @Test
        @DisplayName("should return 401 for bad credentials")
        void shouldRejectBadCredentials() throws Exception {
            var request = new LoginRequest("user@humg.edu.vn", "wrong");
            given(authService.login(any(LoginRequest.class)))
                    .willThrow(new BadCredentialsException("Invalid credentials"));

            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("should return 400 for blank fields")
        void shouldRejectBlankFields() throws Exception {
            var request = new LoginRequest("", "");
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/auth/refresh")
    class Refresh {

        @Test
        @DisplayName("should refresh token from cookie")
        void shouldRefreshSuccessfully() throws Exception {
            given(authService.refresh(eq("valid-refresh-token"))).willReturn(SAMPLE_RESULT);

            mockMvc.perform(post("/api/v1/auth/refresh")
                            .cookie(new Cookie("refresh_token", "valid-refresh-token")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.accessToken").exists())
                    .andExpect(header().exists("Set-Cookie"));
        }

        @Test
        @DisplayName("should return 422 when refresh cookie is missing")
        void shouldRejectMissingCookie() throws Exception {
            mockMvc.perform(post("/api/v1/auth/refresh"))
                    .andExpect(status().isUnprocessableEntity());
        }

        @Test
        @DisplayName("should return 422 for expired refresh token")
        void shouldRejectExpiredToken() throws Exception {
            given(authService.refresh(eq("expired-token")))
                    .willThrow(new BusinessRuleException("TOKEN_EXPIRED", "Refresh token expired"));

            mockMvc.perform(post("/api/v1/auth/refresh")
                            .cookie(new Cookie("refresh_token", "expired-token")))
                    .andExpect(status().isUnprocessableEntity());
        }
    }

    @Nested
    @DisplayName("POST /api/v1/auth/logout")
    class Logout {

        @Test
        @DisplayName("should logout and clear cookie")
        void shouldLogoutSuccessfully() throws Exception {
            UUID userId = UUID.randomUUID();
            doNothing().when(authService).logout(userId);

            mockMvc.perform(post("/api/v1/auth/logout")
                            .with(jwt().jwt(j -> j.subject(userId.toString()))))
                    .andExpect(status().isNoContent())
                    .andExpect(header().exists("Set-Cookie"));
        }

        @Test
        @DisplayName("should reject unauthenticated logout")
        void shouldRejectUnauthenticated() throws Exception {
            mockMvc.perform(post("/api/v1/auth/logout"))
                    .andExpect(status().is4xxClientError());
        }
    }
}
