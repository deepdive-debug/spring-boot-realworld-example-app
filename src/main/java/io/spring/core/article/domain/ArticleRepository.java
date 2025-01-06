package io.spring.core.article.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ArticleRepository {
  Optional<Article> findBySlug(String slug);

  Page<Article> findAll(PageRequest createdAt);

  void delete(Article article);

  Article save(Article article);
}
