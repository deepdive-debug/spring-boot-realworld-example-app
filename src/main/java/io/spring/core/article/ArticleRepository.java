package io.spring.core.article;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
  Optional<Article> findBySlug(String slug);
}
