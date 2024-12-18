package io.spring.api.article;

import io.spring.api.article.request.UpdateArticleParam;
import io.spring.api.data.ArticleData;
import io.spring.application.ArticleQueryService;
import io.spring.core.user.User;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
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
  private ArticleQueryService articleQueryService;

  @GetMapping
  public ResponseEntity<?> article(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
	  return ResponseEntity.ok(articleResponse(articleQueryService.findBySlug(slug, user)));
  }

  @PutMapping
  public ResponseEntity<?> updateArticle(
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody UpdateArticleParam updateArticleParam) {

    return ResponseEntity.ok(
        articleResponse(
            articleQueryService.updateArticle(slug, user, updateArticleParam)));
  }

  @DeleteMapping
  public ResponseEntity deleteArticle(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
    articleQueryService.deleteArticle(slug, user);
    return ResponseEntity.noContent().build();
  }

  private Map<String, Object> articleResponse(ArticleData articleData) {
    return new HashMap<>() {
		{
			put("article", articleData);
		}
	};
  }
}
