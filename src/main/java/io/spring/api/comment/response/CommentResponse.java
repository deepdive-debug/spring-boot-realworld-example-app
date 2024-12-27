package io.spring.api.comment.response;

import io.spring.api.user.response.UserResponse;
import io.spring.core.comment.Comment;
import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
    UUID id, String body, LocalDateTime createdAt, UserResponse commenter) {
  public static CommentResponse from(Comment comment) {
    return new CommentResponse(
        comment.getId(),
        comment.getBody(),
        comment.getCreatedAt(),
        UserResponse.of(comment.getCommenter()));
  }
}
