package io.spring.core.article;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class ArticleTest {

  @Test
  public void should_get_right_slug() {
    // given
    Article article = Article.create("a new   title", "desc", "body", null);

    // then
    assertThat(article.getSlug(), is("a-new-title"));
  }

  @Test
  public void should_get_right_slug_with_number_in_title() {
    // given
    Article article = Article.create("a new title 2", "desc", "body", null);

    // then
    assertThat(article.getSlug(), is("a-new-title-2"));
  }

  @Test
  public void should_get_lower_case_slug() {
    // given
    Article article = Article.create("A NEW TITLE", "desc", "body", null);

    // then
    assertThat(article.getSlug(), is("a-new-title"));
  }

  @Test
  public void should_handle_other_language() {
    // given
    Article article = Article.create("한글 제목", "desc", "body", null);

    // then
    assertThat(article.getSlug(), is("한글-제목"));
  }

  @Test
  public void should_handle_special_characters() {
    // given
    Article article = Article.create("what?the.hell,w", "desc", "body", null);

    // then
    assertThat(article.getSlug(), is("what-the-hell-w"));
  }
}
