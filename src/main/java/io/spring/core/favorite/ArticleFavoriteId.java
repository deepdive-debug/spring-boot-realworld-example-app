package io.spring.core.favorite;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ArticleFavoriteId implements Serializable {
	private String articleId;
	private String userId;

	public static ArticleFavoriteId of(String articleId, String userId) {
		return ArticleFavoriteId.builder()
			.articleId(articleId)
			.userId(userId)
			.build();
	}
}
