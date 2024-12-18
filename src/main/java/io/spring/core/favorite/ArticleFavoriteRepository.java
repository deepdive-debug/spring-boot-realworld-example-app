package io.spring.core.favorite;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite, ArticleFavoriteId> {
}
