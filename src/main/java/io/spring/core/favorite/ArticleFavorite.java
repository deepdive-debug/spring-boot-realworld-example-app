package io.spring.core.favorite;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class ArticleFavorite {

  @EmbeddedId
  private ArticleFavoriteId id;

  public static ArticleFavorite create(String articleId, String userId) {
    ArticleFavoriteId articleFavoriteId = ArticleFavoriteId.of(articleId, userId);
    return ArticleFavorite.builder()
        .id(articleFavoriteId)
        .build();
  }
}
