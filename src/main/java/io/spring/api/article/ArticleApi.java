package io.spring.api.article;

import io.spring.api.article.request.UpdateArticleParam;
import io.spring.api.article.response.ArticleResponse;
import io.spring.application.ArticleQueryService;
import io.spring.application.article.ArticleCommandService;
import io.spring.application.article.ArticleService;
import io.spring.core.user.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/articles/{slug}")
@AllArgsConstructor
public class ArticleApi {
  private ArticleService articleService;

  @GetMapping
  public ResponseEntity<ArticleResponse> article(
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User user
  ) {
    return ResponseEntity.ok(articleService.getArticle(slug, user));
  }

  @PutMapping
  public ResponseEntity<Void> updateArticle(
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody UpdateArticleParam updateArticleParam
  ) {
    articleService.updateArticle(slug, user, updateArticleParam);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping
  public ResponseEntity<Void> deleteArticle(
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User user
  ) {
    articleService.deleteArticle(slug, user);
    return ResponseEntity.noContent().build();
  }
}
