package io.spring.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.spring.api.article.ArticleApi;
import io.spring.api.article.response.ArticleSummaryResponse;
import io.spring.api.common.response.PageableResponse;
import io.spring.api.common.response.PaginatedListResponse;
import io.spring.api.user.response.UserResponse;
import io.spring.application.article.ArticleService;
import io.spring.config.TestSecurityConfig;
import io.spring.core.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArticleApi.class)
@Import({TestSecurityConfig.class})
public class ArticleApiTest {

  @Autowired private MockMvc mvc;

  @MockBean private ArticleService articleService;

  private User user;

  @BeforeEach
  public void setUp() {
    RestAssuredMockMvc.mockMvc(mvc);
    user = User.of("test@test.com", "testUser", "password", "bio", "image");
  }

  @Test
  public void should_get_articles_successfully() {
    int page = 0;
    int size = 10;
    List<ArticleSummaryResponse> articles =
        List.of(
            new ArticleSummaryResponse(
                "slug1", "Title1", LocalDateTime.now(), UserResponse.of(user), 3, 5),
            new ArticleSummaryResponse(
                "slug2", "Title2", LocalDateTime.now(), UserResponse.of(user), 2, 1));

    PageRequest pageable = PageRequest.of(page, size);

    PaginatedListResponse<ArticleSummaryResponse> paginatedResponse =
        PaginatedListResponse.of(articles, PageableResponse.of(pageable, articles));

    when(articleService.getArticles(page, size)).thenReturn(paginatedResponse);

    given()
        .queryParam("page", page)
        .queryParam("size", size)
        .when()
        .get("/articles")
        .then()
        .statusCode(200)
        .body("contents[0].slug", equalTo("slug1"))
        .body("contents[0].tagCount", equalTo(3))
        .body("contents[0].commentCount", equalTo(5))
        .body("contents[1].slug", equalTo("slug2"))
        .body("contents[1].tagCount", equalTo(2))
        .body("contents[1].commentCount", equalTo(1));
  }

  //  @Test
  public void should_get_article_summary_successfully() {
    String slug = "test-article";
    ArticleSummaryResponse articleSummary =
        new ArticleSummaryResponse(
            slug, "Test Title", LocalDateTime.now(), UserResponse.of(user), 3, 5);

    when(articleService.getArticles(0, 1))
        .thenReturn(
            PaginatedListResponse.of(
                List.of(articleSummary),
                PageableResponse.of(PageRequest.of(0, 1), List.of(articleSummary))));

    given()
        .queryParam("page", 0)
        .queryParam("size", 1)
        .when()
        .get("/articles")
        .then()
        .statusCode(200)
        .body("contents[0].slug", equalTo(slug))
        .body("contents[0].title", equalTo("Test Title"))
        .body("contents[0].tagCount", equalTo(3))
        .body("contents[0].commentCount", equalTo(5));
  }
}
