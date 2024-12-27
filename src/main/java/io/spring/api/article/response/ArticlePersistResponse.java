package io.spring.api.article.response;

public record ArticlePersistResponse(String slug) {
  public static ArticlePersistResponse of(String slug) {
    return new ArticlePersistResponse(slug);
  }
}
