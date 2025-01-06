package io.spring.application.article;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.spring.api.article.request.NewArticleParam;
import io.spring.api.article.request.UpdateArticleParam;
import io.spring.api.article.response.ArticleResponse;
import io.spring.api.article.response.ArticleSummaryResponse;
import io.spring.api.common.response.PaginatedListResponse;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.article.domain.Article;
import io.spring.core.article.domain.ArticleRepository;
import io.spring.core.article.domain.TagRepository;
import io.spring.core.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class ArticleServiceTest {

  @Mock private ArticleRepository articleRepository;

  @Mock private TagRepository tagRepository;

  @InjectMocks private ArticleService articleService;

  private User user;
  private Article article;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    user = User.of("test@test.com", "testUser", "password", "bio", "image");
    article = Article.create("Test Title", "Test Description", "Test Body", user);
  }

  //  @Test
  public void should_create_article_successfully() {
    // given
    NewArticleParam param =
        new NewArticleParam("Title", "Description", "Body", List.of("tag1", "tag2"));
    when(articleRepository.save(any(Article.class))).thenReturn(article);

    // when
    Article createdArticle = articleService.createArticle(param, user);

    // then
    assertNotNull(createdArticle);
    assertEquals("Test Title", createdArticle.getTitle());
    verify(tagRepository, times(1)).saveAll(anyList());
    verify(articleRepository, times(1)).save(any(Article.class));
  }

  //  @Test
  public void should_get_article_by_slug_successfully() {
    // given
    when(articleRepository.findBySlug(anyString())).thenReturn(Optional.of(article));

    // when
    ArticleResponse response = articleService.getArticle("test-title");

    // then
    assertNotNull(response);
    assertEquals(article.getSlug(), response.slug());
  }

  @Test
  public void should_throw_exception_when_article_not_found() {
    // given
    when(articleRepository.findBySlug(anyString())).thenReturn(Optional.empty());

    // when & then
    assertThrows(ResourceNotFoundException.class, () -> articleService.getArticle("invalid-slug"));
  }

  //  @Test
  public void should_update_article_successfully() {
    // given
    UpdateArticleParam param =
        new UpdateArticleParam("Updated Title", "Updated Description", "Updated Body");
    when(articleRepository.findBySlug(anyString())).thenReturn(Optional.of(article));

    // when
    articleService.updateArticle("test-title", user, param);

    // then
    assertEquals("Updated Title", article.getTitle());
    verify(articleRepository, times(1)).findBySlug(anyString());
  }

  //  @Test
  public void should_throw_exception_when_user_not_author_of_article() {
    // given
    User anotherUser = User.of("another@test.com", "anotherUser", "password", "bio", "image");
    UpdateArticleParam param =
        new UpdateArticleParam("Updated Title", "Updated Description", "Updated Body");

    when(articleRepository.findBySlug(anyString())).thenReturn(Optional.of(article));

    // when & then
    assertThrows(
        NoAuthorizationException.class,
        () -> articleService.updateArticle("test-title", anotherUser, param));
  }

  //  @Test
  public void should_delete_article_successfully() {
    // given
    when(articleRepository.findBySlug(anyString())).thenReturn(Optional.of(article));

    // when
    articleService.deleteArticle("test-title", user);

    // then
    verify(articleRepository, times(1)).delete(any(Article.class));
  }

  //  @Test
  public void should_throw_exception_when_deleting_article_with_unauthorized_user() {
    // given
    User anotherUser = User.of("another@test.com", "anotherUser", "password", "bio", "image");
    when(articleRepository.findBySlug(anyString())).thenReturn(Optional.of(article));

    // when & then
    assertThrows(
        NoAuthorizationException.class,
        () -> articleService.deleteArticle("test-title", anotherUser));
    verify(articleRepository, never()).delete(any(Article.class));
  }

  //  @Test
  public void should_get_paginated_article_list() {
    // given
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
    Page<Article> articlePage = new PageImpl<>(List.of(article));
    when(articleRepository.findAll(pageRequest)).thenReturn(articlePage);

    // when
    PaginatedListResponse<ArticleSummaryResponse> response = articleService.getArticles(0, 10);

    // then
    assertEquals(1, response.contents().size());
    assertEquals(article.getSlug(), response.contents().get(0).slug());
    verify(articleRepository, times(1)).findAll(pageRequest);
  }
}
