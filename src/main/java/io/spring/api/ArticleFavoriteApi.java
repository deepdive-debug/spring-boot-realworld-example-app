package io.spring.api;

import io.spring.application.ArticleQueryService;
import io.spring.application.data.ArticleData;
import io.spring.core.article.Article;
import io.spring.core.favorite.ArticleFavorite;
import io.spring.core.user.User;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/articles/{slug}/favorite")
@AllArgsConstructor
public class ArticleFavoriteApi {
  private ArticleQueryService articleQueryService;

  @PostMapping
  public ResponseEntity favoriteArticle(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
      Article article = articleQueryService.findBySlug(slug);
    ArticleFavorite articleFavorite = ArticleFavorite.of(article.getId(), user.getId());
    articleQueryService.saveArticleFavorite(articleFavorite);
    return responseArticleData(articleQueryService.findBySlug(slug, user).get());
  }

  @DeleteMapping
  public ResponseEntity unfavoriteArticle(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
      Article article = articleQueryService.findBySlug(slug);
      ArticleFavorite favorite = articleQueryService.findArticleFavorite(article.getId(), user.getId());
      articleQueryService.removeArticleFavorite(favorite);
      return responseArticleData(articleQueryService.findBySlug(slug, user).get());
  }

  private ResponseEntity<HashMap<String, Object>> responseArticleData(
      final ArticleData articleData) {
    return ResponseEntity.ok(
        new HashMap<String, Object>() {
          {
            put("article", articleData);
          }
        });
  }
}
