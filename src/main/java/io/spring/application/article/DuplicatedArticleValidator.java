package io.spring.application.article;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ArticleQueryService;
import io.spring.core.article.Article;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

class DuplicatedArticleValidator
    implements ConstraintValidator<DuplicatedArticleConstraint, String> {

  @Autowired private ArticleQueryService articleQueryService;

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    try {
      articleQueryService.findBySlug(Article.toSlug(value), null);
      return false;
    } catch (ResourceNotFoundException e) {
      return true;
    }
  }
}
