package io.spring.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.spring.api.comment.CommentsApi;
import io.spring.api.comment.response.CommentPersistResponse;
import io.spring.application.comment.CommentService;
import io.spring.core.article.Article;
import io.spring.core.comment.Comment;
import io.spring.core.user.User;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CommentsApi.class)
public class CommentsApiTest {

  @Autowired private MockMvc mvc;

  @MockBean private CommentService commentService;

  private Article article;
  private User user;
  private Comment comment;

  @BeforeEach
  public void setUp() {
    // given - 테스트 데이터 초기화
    RestAssuredMockMvc.mockMvc(mvc);
    user = User.of("test@test.com", "testUser", "password", "bio", "image");
    article = Article.create("Test Title", "Test Description", "Test Body", user);
    comment = Comment.create("This is a test comment", user, article);
  }

  //  @Test
  @WithMockUser
  public void should_create_comment_successfully() {
    // given - 준비
    CommentPersistResponse response = new CommentPersistResponse(comment.getId());
    when(commentService.createComment(anyString(), any(User.class), any())).thenReturn(response);

    // when - 요청
    given()
        .contentType("application/json")
        .body(
            """
                {
                  "body": "This is a test comment"
                }
                """)
        .when()
        .post("/articles/{slug}/comments", article.getSlug())

        // then - 검증
        .then()
        .statusCode(201)
        .body("id", equalTo(comment.getId()))
        .body("body", equalTo(comment.getBody()));
  }

  //  @Test
  @WithMockUser
  public void should_update_comment_successfully() {
    // given - 준비
    doNothing().when(commentService).update(UUID.fromString(anyString()), any(User.class), any());

    // when - 요청
    given()
        .contentType("application/json")
        .body(
            """
                {
                  "body": "Updated comment content"
                }
                """)
        .when()
        .patch("/articles/{slug}/comments/{id}", article.getSlug(), comment.getId())

        // then - 검증
        .then()
        .statusCode(204);

    verify(commentService).update(eq(comment.getId()), any(User.class), any());
  }

  //  @Test
  @WithMockUser
  public void should_delete_comment_successfully() {
    // given - 준비
    doNothing().when(commentService).delete(any(User.class), UUID.fromString(anyString()));

    // when - 요청
    given()
        .header("Authorization", "Token testToken")
        .when()
        .delete("/articles/{slug}/comments/{id}", article.getSlug(), comment.getId())

        // then - 검증
        .then()
        .statusCode(204);

    verify(commentService).delete(any(User.class), eq(comment.getId()));
  }

  //  @Test
  @WithMockUser
  public void should_return_403_when_deleting_comment_of_another_user() {
    // given - 준비
    doThrow(new IllegalStateException("You are not authorized to delete this comment"))
        .when(commentService)
        .delete(any(User.class), UUID.fromString(anyString()));

    // when - 요청
    given()
        .header("Authorization", "Token testToken")
        .when()
        .delete("/articles/{slug}/comments/{id}", article.getSlug(), comment.getId())

        // then - 검증
        .then()
        .statusCode(403);
  }
}
