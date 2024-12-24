package io.spring.core.article.infrastructure;

import io.spring.core.article.domain.Article;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaArticleRepository extends JpaRepository<Article, String> {
  Optional<Article> findBySlug(String slug);
}
