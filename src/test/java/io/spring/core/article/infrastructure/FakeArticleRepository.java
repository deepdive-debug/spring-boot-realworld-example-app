package io.spring.core.article.infrastructure;

import io.spring.api.article.response.ArticleSummaryResponse;
import io.spring.core.article.domain.Article;
import io.spring.core.article.domain.ArticleRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class FakeArticleRepository implements ArticleRepository {
  private final List<Article> data = Collections.synchronizedList(new ArrayList<>());

  @Override
  public Optional<Article> findBySlug(String slug) {
    return data.stream().filter(a -> a.getSlug().equals(slug)).findFirst();
  }

  @Override
  public Page<Article> findAll(PageRequest pageRequest) {
    synchronized (data) {
      int start = (int) pageRequest.getOffset();
      int end = Math.min(start + pageRequest.getPageSize(), data.size());

      if (start > end) {
        return new PageImpl<>(Collections.emptyList(), pageRequest, data.size());
      }

      // 페이지에 해당하는 데이터 추출
      List<Article> pageContent = data.subList(start, end);
      return new PageImpl<>(pageContent, pageRequest, data.size());
    }
  }

  @Override
  public void delete(Article article) {
    Optional<Article> foundArticle =
        data.stream().filter(a -> a.getId().equals(article.getId())).findFirst();

    foundArticle.ifPresent(data::remove);
  }

  @Override
  public Article save(Article article) {
    Article newArticle =
        Article.builder()
            .author(article.getAuthor())
            .slug(article.getSlug())
            .title(article.getTitle())
            .description(article.getDescription())
            .body(article.getBody())
            .build();

    data.add(newArticle);
    return newArticle;
  }

  @Override
  public Page<ArticleSummaryResponse> findAllArticleSummary(Pageable pageable) {
    synchronized (data) {
      int start = (int) pageable.getOffset();
      int end = Math.min(start + pageable.getPageSize(), data.size());

      if (start > end) {
        return new PageImpl<>(Collections.emptyList(), pageable, data.size());
      }

      List<ArticleSummaryResponse> pageContent =
          data.subList(start, end).stream().map(ArticleSummaryResponse::from).toList();

      return new PageImpl<>(pageContent, pageable, data.size());
    }
  }
}
