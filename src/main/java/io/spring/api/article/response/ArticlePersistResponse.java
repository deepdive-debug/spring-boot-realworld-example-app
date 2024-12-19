package io.spring.api.article.response;

import lombok.Builder;

@Builder
public record ArticlePersistResponse(String slug) {
  public static ArticlePersistResponse of(String slug) {
    return ArticlePersistResponse.builder().slug(slug).build();
  }
}
