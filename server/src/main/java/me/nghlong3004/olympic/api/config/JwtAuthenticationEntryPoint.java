package me.nghlong3004.olympic.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

/**
 * Custom authentication entry point that returns RFC 7807 ProblemDetail JSON
 * instead of Spring's default HTML error page.
 *
 * <p>Differentiates between expired tokens, invalid tokens, and general
 * authentication failures for better client-side error handling.
 *
 * @author nghlong3004
 * @since 2026-06-05
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String ERROR_BASE_URI = "https://olympic.humg.edu.vn/errors/";

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        log.debug("Authentication failed: {}", authException.getMessage());

        ProblemDetail problem = resolveProblem(authException);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(problem.getStatus());
        objectMapper.writeValue(response.getOutputStream(), problem);
    }

    private ProblemDetail resolveProblem(AuthenticationException ex) {
        if (ex instanceof InvalidBearerTokenException) {
            String message = ex.getMessage();
            if (message != null && message.toLowerCase().contains("expired")) {
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED, "Access token has expired");
                problem.setType(URI.create(ERROR_BASE_URI + "token-expired"));
                problem.setTitle("Token Expired");
                problem.setProperty("errorCode", "ACCESS_TOKEN_EXPIRED");
                return problem;
            }
            ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                    HttpStatus.UNAUTHORIZED, "Invalid access token");
            problem.setType(URI.create(ERROR_BASE_URI + "invalid-token"));
            problem.setTitle("Invalid Token");
            problem.setProperty("errorCode", "INVALID_ACCESS_TOKEN");
            return problem;
        }

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, "Authentication required");
        problem.setType(URI.create(ERROR_BASE_URI + "unauthorized"));
        problem.setTitle("Unauthorized");
        problem.setProperty("errorCode", "UNAUTHORIZED");
        return problem;
    }
}
