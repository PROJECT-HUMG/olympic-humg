package me.nghlong3004.olympic.api.common.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
class ApiResponseTest {

  @Test
  void of_WithSingleObject_WrapsData() {
    // Arrange
    var value = "MATH";

    // Act
    var response = ApiResponse.of(value);

    // Assert
    assertThat(response.data()).isEqualTo("MATH");
  }

  @Test
  void of_WithPagedData_CopiesDataAndPageMetadata() {
    // Arrange
    var data = new ArrayList<>(List.of("MATH", "PHYSICS"));
    var page = new PageMeta(0, 2, 4, 2);

    // Act
    var response = PagedResponse.of(data, page);
    data.add("CHEMISTRY");

    // Assert
    assertThat(response.data()).containsExactly("MATH", "PHYSICS");
    assertThat(response.page()).isEqualTo(page);
  }
}
