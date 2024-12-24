package io.spring.core.article.infrastructure;

import io.spring.core.article.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaArticleRepository extends JpaRepository<Article, String> {
  Optional<Article> findBySlug(String slug);
}
