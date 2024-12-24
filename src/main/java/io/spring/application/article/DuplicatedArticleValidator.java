package io.spring.application.article;

import io.spring.core.article.domain.Article;
import io.spring.core.article.domain.ArticleRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class DuplicatedArticleValidator
    implements ConstraintValidator<DuplicatedArticleConstraint, String> {

  private final ArticleRepository articleRepository;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return articleRepository.findBySlug(Article.toSlug(value)).isEmpty();
  }
}
