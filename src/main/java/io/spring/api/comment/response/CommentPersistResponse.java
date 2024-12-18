package io.spring.api.comment.response;

import io.spring.core.comment.Comment;
import lombok.Builder;

@Builder
public record CommentPersistResponse(
	String id
) {
	public static CommentPersistResponse from(Comment comment) {
		return CommentPersistResponse.builder()
			.id(comment.getId())
			.build();
	}
}
