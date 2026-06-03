package me.nghlong3004.olympic.api.common.response;

import java.util.List;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
public record PagedResponse<T>(List<T> data, PageMeta page) {

  public PagedResponse {
    data = List.copyOf(data);
  }

  public static <T> PagedResponse<T> of(List<T> data, PageMeta page) {
    return new PagedResponse<>(data, page);
  }
}
