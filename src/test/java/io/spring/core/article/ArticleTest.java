package io.spring.core.article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.spring.core.article.domain.Article;
import org.junit.jupiter.api.Test;

public class ArticleTest {
	@Test
	public void articleCreateTest_success() {
		//given
		Article article = Article.create("a new title", "desc", "body", null);

		//then
		assertNotNull(article);
		assertThat(article.getTitle()).isEqualTo("a new title");
	}

	@Test
	public void articleCreateTest_fail() {
		assertThrows(NullPointerException.class,
			() -> Article.create(null, "desc", "body", null)
		);
	}

	@Test
	public void articleUpdateTest() {
		//given
		Article article = Article.create("a new title", "desc", "body", null);

		//when
		article.update("a new title updated", "updated desc", "updated body");

		//then
		assertThat(article.getTitle()).isEqualTo("a new title updated");
		assertThat(article.getDescription()).isEqualTo("updated desc");
		assertThat(article.getBody()).isEqualTo("updated body");
		assertThat(article.getSlug()).isEqualTo("a-new-title-updated");
	}
}
