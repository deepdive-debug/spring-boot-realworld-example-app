package io.spring.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.spring.api.user.response.ProfileData;
import io.spring.application.DateTimeCursor;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class ArticleData implements Node {
  private String id;
  private String slug;
  private String title;
  private String description;
  private String body;
  private boolean favorited;
  private int favoritesCount;
  private Instant createdAt;
  private Instant updatedAt;
  private List<String> tagList;

  @JsonProperty("author")
  private ProfileData profileData;

  @Override
  public DateTimeCursor getCursor() {
    return new DateTimeCursor(updatedAt);
  }

  public void updateFavorited(boolean favorited) {
    this.favorited = favorited;
  }

  public void updateFavoritesCount(int favoritesCount) {
    this.favoritesCount = favoritesCount;
  }
}
