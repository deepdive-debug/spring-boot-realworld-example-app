package io.spring.core.article;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.spring.core.article.domain.Article;
import org.junit.jupiter.api.Test;

public class ArticleSlugTest {

  @Test
  public void shouldGetRightSlug() {
    // given
    Article article = Article.create("a new   title", "desc", "body", null);

    // then
    assertThat(article.getSlug(), is("a-new-title"));
  }

  @Test
  public void shouldGetRightSlugWithNumberInTitle() {
    // given
    Article article = Article.create("a new title 2", "desc", "body", null);

    // then
    assertThat(article.getSlug(), is("a-new-title-2"));
  }

  @Test
  public void shouldGetLowerCaseSlug() {
    // given
    Article article = Article.create("A NEW TITLE", "desc", "body", null);

    // then
    assertThat(article.getSlug(), is("a-new-title"));
  }

  @Test
  public void shouldHandleOtherLanguage() {
    // given
    Article article = Article.create("한글 제목", "desc", "body", null);

    // then
    assertThat(article.getSlug(), is("한글-제목"));
  }

  @Test
  public void shouldHandleSpecialCharacters() {
    // given
    Article article = Article.create("what?the.hell,w", "desc", "body", null);

    // then
    assertThat(article.getSlug(), is("what-the-hell-w"));
  }
}
