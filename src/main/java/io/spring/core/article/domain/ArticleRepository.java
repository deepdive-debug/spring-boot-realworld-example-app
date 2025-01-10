package io.spring.core.article.domain;

import io.spring.api.article.response.ArticleSummaryResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public interface ArticleRepository {
  Optional<Article> findBySlug(String slug);

  Page<Article> findAll(PageRequest createdAt);

  void delete(Article article);

  Article save(Article article);

  Page<ArticleSummaryResponse> findAllArticleSummary(Pageable pageable);

  Page<ArticleSummaryResponse> findAllByCreatedAtBetween(
      LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
