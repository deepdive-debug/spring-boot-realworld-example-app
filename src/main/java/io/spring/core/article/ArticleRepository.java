package io.spring.core.article;

import io.spring.api.article.response.ArticleSummaryResponse;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, UUID> {
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

  Optional<Article> findBySlug(String slug);
}
