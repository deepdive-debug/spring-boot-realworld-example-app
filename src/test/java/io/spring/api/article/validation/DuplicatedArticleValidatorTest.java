package io.spring.api.article.validation;

import io.spring.core.article.domain.Article;
import io.spring.core.article.domain.ArticleRepository;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DuplicatedArticleValidatorTest {

	@Mock
	private ArticleRepository articleRepository;

	@Mock
	private ConstraintValidatorContext context;

	private DuplicatedArticleValidator validator;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		validator = new DuplicatedArticleValidator(articleRepository);
	}

	@Test
	void shouldReturnTrueWhenSlugIsUnique() {
		// Given
		String articleTitle = "Unique Title";
		String slug = "unique-title";
		when(articleRepository.findBySlug(slug)).thenReturn(Optional.empty());

		// When
		boolean isValid = validator.isValid(articleTitle, context);

		// Then
		assertThat(isValid).isTrue();
		Mockito.verify(articleRepository).findBySlug(slug);
	}

	@Test
	void shouldReturnFalseWhenSlugAlreadyExists() {
		// Given
		String articleTitle = "Duplicate Title";
		String slug = "duplicate-title";
		when(articleRepository.findBySlug(slug)).thenReturn(Optional.of(Mockito.mock(Article.class)));

		// When
		boolean isValid = validator.isValid(articleTitle, context);

		// Then
		assertThat(isValid).isFalse();
		Mockito.verify(articleRepository).findBySlug(slug);
	}
}
