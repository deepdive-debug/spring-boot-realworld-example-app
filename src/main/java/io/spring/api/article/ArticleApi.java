package io.spring.api.article;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.HashMap;

import io.spring.api.article.request.NewArticleParam;
import io.spring.api.article.request.UpdateArticleParam;
import io.spring.api.article.response.ArticlePersistResponse;
import io.spring.api.article.response.ArticleResponse;
import io.spring.api.article.response.ArticleSummaryResponse;
import io.spring.api.common.response.PaginatedListResponse;
import io.spring.application.article.ArticleService;
import io.spring.core.article.Article;
import io.spring.core.user.User;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/articles")
@AllArgsConstructor
public class ArticleApi {
  private ArticleService articleService;

  @GetMapping
  public ResponseEntity<PaginatedListResponse<ArticleSummaryResponse>> getArticles(

      @Parameter(description = "페이지 인덱스", example = "0", required = true) @RequestParam(defaultValue = "0") @PositiveOrZero int page,
      @Parameter(description = "응답 개수", example = "10", required = true) @RequestParam(defaultValue = "10") @Positive int size
  ){
    return ResponseEntity.ok(articleService.getArticles(page, size));
  }

  @PostMapping
  public ResponseEntity<ArticlePersistResponse> createArticle(
      @Valid @RequestBody NewArticleParam newArticleParam, @AuthenticationPrincipal User user) {
    Article article = articleService.createArticle(newArticleParam, user);
    return ResponseEntity.status(CREATED).body(ArticlePersistResponse.of(article.getSlug()));
  }


  @GetMapping("/{slug}")
  public ResponseEntity<ArticleResponse> article(
      @PathVariable("slug") String slug
  ) {
    return ResponseEntity.ok(articleService.getArticle(slug));
  }

  @PutMapping("/{slug}")
  public ResponseEntity<Void> updateArticle(
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody UpdateArticleParam updateArticleParam
  ) {
    articleService.updateArticle(slug, user, updateArticleParam);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{slug}")
  public ResponseEntity<Void> deleteArticle(
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User user
  ) {
    articleService.deleteArticle(slug, user);
    return ResponseEntity.noContent().build();
  }
}
