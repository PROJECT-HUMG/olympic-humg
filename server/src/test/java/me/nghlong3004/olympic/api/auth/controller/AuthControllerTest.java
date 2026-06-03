package me.nghlong3004.olympic.api.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

  @Container
  static final PostgreSQLContainer POSTGRES =
      new PostgreSQLContainer(DockerImageName.parse("postgres:16-alpine"));

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private JdbcTemplate jdbcTemplate;

  @DynamicPropertySource
  static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    registry.add("spring.jpa.open-in-view", () -> "false");
    registry.add("spring.flyway.enabled", () -> "true");
    registry.add("olympic.api.problem-type-base-url", () -> "http://localhost:8080/api/v1/problems");
    registry.add("olympic.client.base-url", () -> "http://localhost:3000");
    registry.add(
        "olympic.security.jwt.secret-key", () -> "test-secret-key-test-secret-key-test-secret-key");
    registry.add("olympic.security.jwt.access-expiration", () -> "15");
    registry.add("olympic.security.jwt.refresh-expiration", () -> "10080");
    registry.add("olympic.security.cookie.secure", () -> "false");
    registry.add("olympic.security.cookie.same-site", () -> "Lax");
    registry.add("spring.security.oauth2.client.registration.google.client-id", () -> "test");
    registry.add("spring.security.oauth2.client.registration.google.client-secret", () -> "test");
    registry.add("spring.security.oauth2.client.registration.github.client-id", () -> "test");
    registry.add("spring.security.oauth2.client.registration.github.client-secret", () -> "test");
  }

  @Test
  void register_ValidStudentRequest_CreatesStudentAndReturnsTokens() throws Exception {
    // Arrange
    var request =
        """
        {
          "email": "student@example.com",
          "password": "StrongPassword123!",
          "fullName": "Nguyen Van A"
        }
        """;

    // Act
    var response =
        mockMvc
            .perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Assert
    JsonNode data = objectMapper.readTree(response).path("data");
    assertThat(data.path("accessToken").asText()).isNotBlank();
    assertThat(data.path("refreshToken").asText()).isNotBlank();
    assertThat(data.path("tokenType").asText()).isEqualTo("Bearer");
    assertThat(data.path("expiresIn").asLong()).isEqualTo(900L);
    assertThat(data.path("user").path("email").asText()).isEqualTo("student@example.com");
    assertThat(data.path("user").path("roles").get(0).asText()).isEqualTo("STUDENT");

    Map<String, Object> userRow =
        jdbcTemplate.queryForMap("select password_hash, status from users where email = ?", "student@example.com");
    assertThat(userRow.get("password_hash")).isNotEqualTo("StrongPassword123!");
    assertThat(userRow.get("status")).isEqualTo("ACTIVE");
  }

  @Test
  void login_ValidCredentials_ReturnsTokensForExistingUser() throws Exception {
    // Arrange
    register("login@example.com", "Login User");
    var request =
        """
        {
          "email": "login@example.com",
          "password": "StrongPassword123!"
        }
        """;

    // Act
    var response =
        mockMvc
            .perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Assert
    JsonNode data = objectMapper.readTree(response).path("data");
    assertThat(data.path("accessToken").asText()).isNotBlank();
    assertThat(data.path("refreshToken").asText()).isNotBlank();
    assertThat(data.path("user").path("roles").get(0).asText()).isEqualTo("STUDENT");
  }

  @Test
  void login_InvalidPassword_ReturnsUnauthorizedProblem() throws Exception {
    // Arrange
    register("bad-login@example.com", "Bad Login User");
    var request =
        """
        {
          "email": "bad-login@example.com",
          "password": "WrongPassword123!"
        }
        """;

    // Act & Assert
    mockMvc
        .perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(request))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void refresh_ValidRefreshToken_RotatesRefreshTokenAndRejectsOldToken() throws Exception {
    // Arrange
    JsonNode auth = register("refresh@example.com", "Refresh User");
    String refreshToken = auth.path("refreshToken").asText();

    // Act
    var response =
        mockMvc
            .perform(
                post("/api/v1/auth/refresh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(refreshRequest(refreshToken)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Assert
    JsonNode data = objectMapper.readTree(response).path("data");
    assertThat(data.path("accessToken").asText()).isNotBlank();
    assertThat(data.path("refreshToken").asText()).isNotEqualTo(refreshToken);

    mockMvc
        .perform(
            post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(refreshRequest(refreshToken)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void logout_AuthenticatedUser_RevokesRefreshToken() throws Exception {
    // Arrange
    JsonNode auth = register("logout@example.com", "Logout User");
    String accessToken = auth.path("accessToken").asText();
    String refreshToken = auth.path("refreshToken").asText();

    // Act
    mockMvc
        .perform(
            post("/api/v1/auth/logout")
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(refreshRequest(refreshToken)))
        .andExpect(status().isNoContent());

    // Assert
    mockMvc
        .perform(
            post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(refreshRequest(refreshToken)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void logout_MissingAccessToken_ReturnsUnauthorized() throws Exception {
    // Arrange
    JsonNode auth = register("logout-missing-token@example.com", "Logout Missing Token User");
    String refreshToken = auth.path("refreshToken").asText();

    // Act & Assert
    mockMvc
        .perform(
            post("/api/v1/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(refreshRequest(refreshToken)))
        .andExpect(status().isUnauthorized());
  }

  private JsonNode register(String email, String fullName) throws Exception {
    var request =
        """
        {
          "email": "%s",
          "password": "StrongPassword123!",
          "fullName": "%s"
        }
        """
            .formatted(email, fullName);

    var response =
        mockMvc
            .perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(request))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

    return objectMapper.readTree(response).path("data");
  }

  private String refreshRequest(String refreshToken) {
    return """
        {
          "refreshToken": "%s"
        }
        """
        .formatted(refreshToken);
  }
}
