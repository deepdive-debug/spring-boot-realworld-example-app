package io.spring.core.favorite;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class ArticleFavorite {

  private String articleId;
  private String userId;

  @Builder(access = AccessLevel.PRIVATE)
  private ArticleFavorite(String articleId, String userId) {
    this.articleId = articleId;
    this.userId = userId;
  }

  public static ArticleFavorite of(String articleId, String userId) {
    return ArticleFavorite
        .builder()
        .articleId(articleId)
        .userId(userId)
        .build();
  }
}
