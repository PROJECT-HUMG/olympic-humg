package me.nghlong3004.olympic.api.auth.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import me.nghlong3004.olympic.api.user.domain.Permission;
import me.nghlong3004.olympic.api.user.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
@Service
@RequiredArgsConstructor
public class TokenService {

  private final JwtEncoder jwtEncoder;
  private final SecureRandom secureRandom = new SecureRandom();

  @Value("${olympic.security.jwt.access-expiration}")
  private long accessExpirationMinutes;

  @Value("${olympic.security.jwt.refresh-expiration}")
  private long refreshExpirationMinutes;

  public String createAccessToken(User user) {
    Instant now = Instant.now();
    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("olympic-api")
            .subject(user.getId().toString())
            .issuedAt(now)
            .expiresAt(now.plus(accessTokenTtl()))
            .claim("email", user.getEmail())
            .claim("scope", scope(user))
            .build();
    JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
    return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
  }

  public String createRefreshToken() {
    byte[] bytes = new byte[32];
    secureRandom.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  public Duration accessTokenTtl() {
    return Duration.ofMinutes(accessExpirationMinutes);
  }

  public Duration refreshTokenTtl() {
    return Duration.ofMinutes(refreshExpirationMinutes);
  }

  public long accessExpiresInSeconds() {
    return accessTokenTtl().toSeconds();
  }

  private String scope(User user) {
    return user.getRoles().stream()
        .flatMap(
            role ->
                java.util.stream.Stream.concat(
                    java.util.stream.Stream.of(role.getCode()),
                    role.getPermissions().stream().map(Permission::getCode)))
        .distinct()
        .sorted()
        .reduce((left, right) -> left + " " + right)
        .orElse("");
  }
}
