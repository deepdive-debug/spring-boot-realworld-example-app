package io.spring.application.article;

import java.util.List;

import io.spring.api.article.request.NewArticleParam;
import io.spring.api.article.request.UpdateArticleParam;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.article.Tag;
import io.spring.core.article.TagRepository;
import io.spring.core.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class ArticleCommandService {

  private final ArticleRepository articleRepository;


  @Transactional
  public Article updateArticle(Article article, @Valid UpdateArticleParam updateArticleParam) {
    article.update(
        updateArticleParam.title(), updateArticleParam.description(), updateArticleParam.body());
    return article;
  }
}
