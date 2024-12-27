package io.spring.api.comment.response;

import io.spring.core.comment.Comment;
import java.util.UUID;

public record CommentPersistResponse(UUID id) {
  public static CommentPersistResponse from(Comment comment) {
    return new CommentPersistResponse(comment.getId());
  }
}
