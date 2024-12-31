package io.spring.application.article;

import io.spring.api.article.request.NewArticleParam;
import io.spring.api.article.request.UpdateArticleParam;
import io.spring.api.article.response.ArticleResponse;
import io.spring.api.article.response.ArticleSummaryResponse;
import io.spring.api.common.response.PageableResponse;
import io.spring.api.common.response.PaginatedListResponse;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.article.Tag;
import io.spring.core.article.TagRepository;
import io.spring.core.user.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ArticleService {
  private final ArticleRepository articleRepository;
  private final TagRepository tagRepository;

  @Transactional(readOnly = true)
  public ArticleResponse getArticle(String slug) {
    Article article = findBySlug(slug);

    return ArticleResponse.from(article);
  }

  @Transactional(readOnly = true)
  public PaginatedListResponse<ArticleSummaryResponse> getArticles(int page, int size) {
    Page<ArticleSummaryResponse> articlePage =
        articleRepository.findAllArticleSummary(
            PageRequest.of(page, size, Sort.by("createdAt").descending()));
    return PaginatedListResponse.of(
        articlePage.getContent(),
        PageableResponse.of(articlePage.getPageable(), articlePage.getContent()));
  }

  @Transactional
  public void updateArticle(String slug, User user, UpdateArticleParam updateArticleParam) {
    Article article = findBySlug(slug);
    isAuthor(user, article);
    article.update(
        updateArticleParam.title(), updateArticleParam.description(), updateArticleParam.body());
  }

  @Transactional
  public void deleteArticle(String slug, User user) {
    Article article = findBySlug(slug);
    isAuthor(user, article);
    articleRepository.delete(article);
  }

  Article findBySlug(String slug) {
    return articleRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
  }

  private void isAuthor(User user, Article article) {
    if (!user.getId().equals(article.getAuthor().getId())) {
      throw new NoAuthorizationException();
    }
  }

  @Transactional
  public Article createArticle(@Valid NewArticleParam newArticleParam, User creator) {
    Article article =
        Article.create(
            newArticleParam.title(),
            newArticleParam.description(),
            newArticleParam.body(),
            creator);

    articleRepository.save(article);

    List<Tag> tags =
        newArticleParam.tagList().stream().map(tag -> Tag.create(tag, article)).toList();

    tagRepository.saveAll(tags);

    return article;
  }
}
