package io.spring.core.article.infrastructure;

import io.spring.api.article.response.ArticleSummaryResponse;
import io.spring.core.article.domain.Article;
import io.spring.core.article.domain.ArticleRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepository {
  private final JpaArticleRepository jpaArticleRepository;

  @Override
  public Optional<Article> findBySlug(String slug) {
    return jpaArticleRepository.findBySlug(slug);
  }

  @Override
  public Page<Article> findAll(PageRequest createdAt) {
    return jpaArticleRepository.findAll(createdAt);
  }

  @Override
  public void delete(Article article) {
    jpaArticleRepository.delete(article);
  }

  @Override
  public Article save(Article article) {
    return jpaArticleRepository.save(article);
  }

  @Override
  public Page<ArticleSummaryResponse> findAllArticleSummary(Pageable pageable) {
    return jpaArticleRepository.findAllArticleSummary(pageable);
  }
}
