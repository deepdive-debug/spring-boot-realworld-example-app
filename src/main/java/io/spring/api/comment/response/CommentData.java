package io.spring.api.comment.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.spring.application.DateTimeCursor;
import java.time.Instant;

import io.spring.api.user.response.ProfileData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class CommentData implements Node {
  private String id;
  private String body;
  @JsonIgnore private String articleId;
  private Instant createdAt;
  private Instant updatedAt;

  @JsonProperty("author")
  private ProfileData profileData;

  @Override
  public DateTimeCursor getCursor() {
    return new DateTimeCursor(createdAt);
  }
}
