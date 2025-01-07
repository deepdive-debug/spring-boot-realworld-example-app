package io.spring.core.article.infrastructure;

import io.spring.api.article.response.ArticleSummaryResponse;
import io.spring.core.article.domain.Article;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaArticleRepository extends JpaRepository<Article, UUID> {
  Optional<Article> findBySlug(String slug);

  @Query(
      """
    SELECT new io.spring.api.article.response.ArticleSummaryResponse(
        a.slug,
        a.title,
        a.createdAt,
        new io.spring.api.user.response.UserResponse(
            u.id,
            u.username,
            u.bio,
            u.image
        ),
        (SELECT COUNT(t) FROM Tag t WHERE t.article.id = a.id),
        (SELECT COUNT(c) FROM Comment c WHERE c.article.id = a.id)
    )
    FROM Article a
    JOIN a.author u
    """)
  Page<ArticleSummaryResponse> findAllArticleSummary(Pageable pageable);
}
