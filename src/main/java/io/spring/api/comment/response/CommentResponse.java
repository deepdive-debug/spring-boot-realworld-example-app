package io.spring.api.comment.response;

import io.spring.api.user.response.UserResponse;
import io.spring.core.comment.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
	String id,
	String body,
	LocalDateTime createdAt,
	UserResponse commenter
) {
	public static CommentResponse of(Comment comment) {
		return new CommentResponse(
			comment.getId(),
			comment.getBody(),
			comment.getCreatedAt(),
			UserResponse.of(comment.getCommenter())
		);
	}
}
