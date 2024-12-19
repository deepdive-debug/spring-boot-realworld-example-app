package io.spring.api.article.response;

import io.spring.api.user.response.UserResponse;
import io.spring.core.article.Article;
import java.time.LocalDateTime;

public record ArticleSummaryResponse(
    String slug,
    String title,
    LocalDateTime createdAt,
    UserResponse author,
    int tagCount,
    int commentCount) {
  public static ArticleSummaryResponse from(Article article) {
    return new ArticleSummaryResponse(
        article.getSlug(),
        article.getTitle(),
        article.getCreatedAt(),
        UserResponse.of(article.getAuthor()),
        article.getTags().size(),
        article.getComments().size());
  }
}
