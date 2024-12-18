package io.spring.application.article;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.spring.api.article.request.UpdateArticleParam;
import io.spring.api.article.response.ArticleResponse;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.user.User;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArticleService {
	private final ArticleRepository articleRepository;

	@Transactional(readOnly = true)
	public ArticleResponse getArticle(String slug, User user) {
		Article article = findBySlug(slug);

		return ArticleResponse.from(article);
	}

	@Transactional
	public void updateArticle(String slug, User user, UpdateArticleParam updateArticleParam) {
		Article article = findBySlug(slug);
		isAuthor(user, article);
		article.update(updateArticleParam.title(), updateArticleParam.description(), updateArticleParam.body());
	}

	@Transactional
	public void deleteArticle(String slug, User user) {
		Article article = findBySlug(slug);
		isAuthor(user, article);
		articleRepository.delete(article);
	}

	private Article findBySlug(String slug) {
		return articleRepository.findBySlug(slug)
			.orElseThrow(ResourceNotFoundException::new);
	}

	private void isAuthor(User user, Article article) {
		if(!user.getId().equals(article.getAuthor().getId())){
			throw new NoAuthorizationException();
		}
	}
}
