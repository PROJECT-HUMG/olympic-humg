package me.nghlong3004.olympic.api.common.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
class ErrorCodeFoundationTest {

  @Test
  void resourceConflict_UsesHttp409() {
    // Arrange + Act
    var errorCode = ErrorCode.RESOURCE_CONFLICT;

    // Assert
    assertThat(errorCode.getStatus()).isEqualTo(409);
    assertThat(errorCode.getCode()).isEqualTo("RESOURCE_CONFLICT");
  }

  @Test
  void businessRuleViolation_UsesHttp422() {
    // Arrange + Act
    var errorCode = ErrorCode.BUSINESS_RULE_VIOLATION;

    // Assert
    assertThat(errorCode.getStatus()).isEqualTo(422);
    assertThat(errorCode.getCode()).isEqualTo("BUSINESS_RULE_VIOLATION");
  }
}
