package me.nghlong3004.olympic.api.common.response;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
public record ApiResponse<T>(T data) {

  public static <T> ApiResponse<T> of(T data) {
    return new ApiResponse<>(data);
  }
}
