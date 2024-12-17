package io.spring.core.comment;

import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {
  private String id;
  private String body;
  private String userId;
  private String articleId;
  private Instant createdAt;

  @Builder(access = AccessLevel.PRIVATE)
  private Comment(String id, String body, String userId, String articleId, Instant createdAt) {
    this.id = id;
    this.body = body;
    this.userId = userId;
    this.articleId = articleId;
    this.createdAt = createdAt;
  }

  public static Comment of(String body, String userId, String articleId) {
    return Comment.builder()
        .id(UUID.randomUUID().toString())
        .body(body)
        .userId(userId)
        .articleId(articleId)
        .createdAt(Instant.now())
        .build();
  }
}
