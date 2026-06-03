package me.nghlong3004.olympic.api.common.pagination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import me.nghlong3004.olympic.api.common.exception.ErrorCode;
import me.nghlong3004.olympic.api.common.exception.ResourceException;
import org.junit.jupiter.api.Test;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
class PageRequestFactoryTest {

  @Test
  void create_WithValidInput_ReturnsPageRequest() {
    // Arrange
    var page = 1;
    var size = 50;

    // Act
    var pageRequest = PageRequestFactory.create(page, size);

    // Assert
    assertThat(pageRequest.getPageNumber()).isEqualTo(1);
    assertThat(pageRequest.getPageSize()).isEqualTo(50);
  }

  @Test
  void create_WithSizeAboveMax_ThrowsValidationError() {
    // Arrange
    var page = 0;
    var size = 101;

    // Act + Assert
    assertThatThrownBy(() -> PageRequestFactory.create(page, size))
        .isInstanceOf(ResourceException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.VALIDATION_ERROR);
  }
}
