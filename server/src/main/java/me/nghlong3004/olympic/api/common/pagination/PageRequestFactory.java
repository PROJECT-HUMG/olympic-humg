package me.nghlong3004.olympic.api.common.pagination;

import java.util.List;
import me.nghlong3004.olympic.api.common.exception.ErrorCode;
import me.nghlong3004.olympic.api.common.exception.ProblemFieldError;
import me.nghlong3004.olympic.api.common.exception.ResourceException;
import org.springframework.data.domain.PageRequest;

/**
 * @author nghlong3004 (Nguyen Hoang Long)
 * @since 6/3/2026
 */
public final class PageRequestFactory {
  public static final int DEFAULT_PAGE = 0;
  public static final int DEFAULT_SIZE = 20;
  public static final int MAX_SIZE = 100;

  private PageRequestFactory() {}

  public static PageRequest create(Integer page, Integer size) {
    int resolvedPage = page == null ? DEFAULT_PAGE : page;
    int resolvedSize = size == null ? DEFAULT_SIZE : size;

    if (resolvedPage < 0) {
      throw new ResourceException(
          ErrorCode.VALIDATION_ERROR,
          List.of(new ProblemFieldError("page", "Page number must be greater than or equal to 0.")));
    }

    if (resolvedSize < 1 || resolvedSize > MAX_SIZE) {
      throw new ResourceException(
          ErrorCode.VALIDATION_ERROR,
          List.of(new ProblemFieldError("size", "Page size must be between 1 and 100.")));
    }

    return PageRequest.of(resolvedPage, resolvedSize);
  }
}
