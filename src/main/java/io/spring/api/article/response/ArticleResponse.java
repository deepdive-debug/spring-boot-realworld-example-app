package io.spring.api.article.response;

import io.spring.api.article.response.tag.TagResponse;
import io.spring.api.comment.response.CommentResponse;
import io.spring.api.user.response.UserResponse;
import io.spring.core.article.Article;
import java.time.LocalDateTime;
import java.util.List;

public record ArticleResponse(
	String slug,
	String title,
	String description,
	String body,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	UserResponse author,
	List<TagResponse> tags,
	List<CommentResponse> comments
) {
	public static ArticleResponse from(Article article) {
		return new ArticleResponse(
			article.getSlug(),
			article.getTitle(),
			article.getDescription(),
			article.getBody(),
			article.getCreatedAt(),
			article.getUpdatedAt(),
			UserResponse.of(article.getAuthor()),
			article.getTags().stream().map(TagResponse::of).toList(),
			article.getComments().stream().map(CommentResponse::of).toList()
		);
	}
}
