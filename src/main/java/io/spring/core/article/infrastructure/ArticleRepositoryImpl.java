package io.spring.core.article.infrastructure;

import io.spring.core.article.domain.Article;
import io.spring.core.article.domain.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
}
