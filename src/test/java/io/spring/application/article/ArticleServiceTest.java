package io.spring.application.article;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.spring.api.article.request.NewArticleParam;
import io.spring.api.article.request.UpdateArticleParam;
import io.spring.api.article.response.ArticleResponse;
import io.spring.api.article.response.ArticleSummaryResponse;
import io.spring.api.common.response.PaginatedListResponse;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.article.domain.Article;
import io.spring.core.article.infrastructure.FakeArticleRepository;
import io.spring.core.article.infrastructure.FakeTagRepository;
import io.spring.core.user.domain.User;
import java.util.List;

import io.spring.core.user.infrastructure.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ArticleServiceTest {
  private ArticleService articleService;

  private User user;
  private Article article;

  @BeforeEach
  void setUp() {
    FakeArticleRepository fakeArticleRepository = new FakeArticleRepository();
    FakeTagRepository fakeTagRepository = new FakeTagRepository();
    FakeUserRepository fakeUserRepository = new FakeUserRepository();
    this.articleService = new ArticleService(fakeArticleRepository, fakeTagRepository);

    user = User.of("test@test.com", "testUser", "password", "bio", "image");
    user = fakeUserRepository.save(user);

    article = Article.create("Test Title", "Test Description", "Test Body", user);
    fakeArticleRepository.save(article);
  }

  @Test
  public void should_create_article_successfully() {
    // given
    NewArticleParam param =
        new NewArticleParam("Title", "Description", "Body", List.of("tag1", "tag2"));

    // when
    Article createdArticle = articleService.createArticle(param, user);

    // then
    assertNotNull(createdArticle);
    assertEquals("Title", createdArticle.getTitle());
  }

  @Test
  public void should_get_article_by_slug_successfully() {
    // when
    ArticleResponse response = articleService.getArticle("test-title");

    // then
    assertNotNull(response);
    assertEquals(article.getSlug(), response.slug());
  }

  @Test
  public void should_throw_exception_when_article_not_found() {
    // when & then
    assertThrows(ResourceNotFoundException.class, () -> articleService.getArticle("invalid-slug"));
  }

//  @Test
  public void should_update_article_successfully() {
    // given
    UpdateArticleParam param =
        new UpdateArticleParam("Updated Title", "Updated Description", "Updated Body");

    // when
    articleService.updateArticle("test-title", user, param);

    // then
    assertEquals("Updated Title", article.getTitle());
  }

//  	@Test
  public void should_throw_exception_when_user_not_author_of_article() {
    // given
    User anotherUser = User.of("another@test.com", "anotherUser", "password", "bio", "image");
    UpdateArticleParam param =
        new UpdateArticleParam("Updated Title", "Updated Description", "Updated Body");

    // when & then
    assertThrows(
        NoAuthorizationException.class,
        () -> articleService.updateArticle("test-title", anotherUser, param));
  }

  //	@Test
  public void should_delete_article_successfully() {
    // when
    articleService.deleteArticle("test-title", user);

    // then
    assertThrows(ResourceNotFoundException.class, () -> articleService.getArticle("test-title"));
  }

  //	@Test
  public void should_throw_exception_when_deleting_article_with_unauthorized_user() {
    // given
    User anotherUser = User.of("another@test.com", "anotherUser", "password", "bio", "image");

    // when & then
    assertThrows(
        NoAuthorizationException.class,
        () -> articleService.deleteArticle("test-title", anotherUser));
  }

  @Test
  public void should_get_paginated_article_list() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
    Page<Article> articlePage = new PageImpl<>(List.of(article));

    // when
    PaginatedListResponse<ArticleSummaryResponse> response = articleService.getArticles(0, 10);

    // then
    assertEquals(1, response.contents().size());
    assertEquals(article.getSlug(), response.contents().get(0).slug());
  }
}
