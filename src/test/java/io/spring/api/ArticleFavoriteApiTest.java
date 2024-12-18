package io.spring.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.spring.api.data.ArticleData;
import io.spring.api.security.WebSecurityConfig;
import io.spring.api.user.response.ProfileData;
import io.spring.core.article.Article;
import io.spring.core.article.Tag;
import io.spring.core.favorite.ArticleFavorite;
import io.spring.core.user.User;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArticleFavoriteApi.class)
@Import({WebSecurityConfig.class, JacksonCustomizations.class})
public class ArticleFavoriteApiTest extends TestWithCurrentUser {
  @Autowired private MockMvc mvc;

  @MockBean private ArticleQueryService articleQueryService;

  private Article article;

  @BeforeEach
  public void setUp() throws Exception {
    super.setUp();
    RestAssuredMockMvc.mockMvc(mvc);
    User anotherUser = User.of("other@test.com", "other", "123", "", "");
    article = Article.of("title", "desc", "body", Arrays.asList("java"), anotherUser.getId());
    when(articleQueryService.findBySlug(eq(article.getSlug()))).thenReturn(article);
    ArticleData articleData =
        new ArticleData(
            article.getId(),
            article.getSlug(),
            article.getTitle(),
            article.getDescription(),
            article.getBody(),
            true,
            1,
            article.getCreatedAt(),
            article.getUpdatedAt(),
            article.getTags().stream().map(Tag::getName).collect(Collectors.toList()),
            new ProfileData(
                anotherUser.getId(),
                anotherUser.getUsername(),
                anotherUser.getBio(),
                anotherUser.getImage(),
                false));
    when(articleQueryService.findBySlug(eq(articleData.getSlug()), eq(user)))
        .thenReturn(Optional.of(articleData));
  }

  @Test
  public void should_favorite_an_article_success() throws Exception {
    given()
        .header("Authorization", "Token " + token)
        .when()
        .post("/articles/{slug}/favorite", article.getSlug())
        .prettyPeek()
        .then()
        .statusCode(200)
        .body("article.id", equalTo(article.getId()));

    verify(articleQueryService).saveArticleFavorite(any());
  }

  @Test
  public void should_unfavorite_an_article_success() throws Exception {
    when(articleQueryService.findArticleFavorite(eq(article.getId()), eq(user.getId())))
        .thenReturn(ArticleFavorite.of(article.getId(), user.getId()));
    given()
        .header("Authorization", "Token " + token)
        .when()
        .delete("/articles/{slug}/favorite", article.getSlug())
        .prettyPeek()
        .then()
        .statusCode(200)
        .body("article.id", equalTo(article.getId()));
    verify(articleQueryService)
        .removeArticleFavorite(ArticleFavorite.of(article.getId(), user.getId()));
  }
}
