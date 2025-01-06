package io.spring.core.article;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.spring.core.article.domain.Article;
import org.junit.jupiter.api.Test;

public class ArticleTest {
	@Test
	public void articleCreate_success() {
		//given
		Article article = Article.create("a new title", "desc", "body", null);

		//then
		assertNotNull(article);
		assertEquals("a new title", article.getTitle());
	}

	@Test
	public void articleCreate_fail() {
		assertThrows(NullPointerException.class,
			() -> Article.create(null, "desc", "body", null)
		);
	}

	@Test
	public void articleUpdate_success() {
		//given
		Article article = Article.create("a new title", "desc", "body", null);

		//when
		article.update("a new title updated", "updated desc", "updated body");

		//then
		assertEquals("a new title updated", article.getTitle());
		assertEquals("updated desc", article.getDescription());
		assertEquals("updated body", article.getBody());
		assertEquals("a-new-title-updated", article.getSlug());
	}
}
