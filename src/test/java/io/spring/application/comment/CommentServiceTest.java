package io.spring.application.comment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.spring.api.comment.request.NewCommentParam;
import io.spring.api.comment.response.CommentPersistResponse;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.article.domain.Article;
import io.spring.core.article.infrastructure.FakeArticleRepository;
import io.spring.core.comment.domain.Comment;
import io.spring.core.comment.infrastructure.FakeCommentRepository;
import io.spring.core.user.domain.User;
import io.spring.core.user.infrastructure.FakeUserRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommentServiceTest {
  private CommentService commentService;
  private User user;
  private User anotherUser;
  private Article article;
  private Comment comment;

  @BeforeEach
  public void setUp() {
    FakeCommentRepository fakecommentRepository = new FakeCommentRepository();
    FakeArticleRepository fakeArticleRepository = new FakeArticleRepository();
    FakeUserRepository userRepository = new FakeUserRepository();
    commentService = new CommentService(fakecommentRepository, fakeArticleRepository);

    // 테스트 데이터 초기화
    user = User.of("test@test.com", "testUser", "password", "bio", "image");
    user = userRepository.save(user);

    anotherUser = User.of("other@test.com", "otherUser", "password", "", "");
    userRepository.save(anotherUser);

    article = Article.create("Test Title", "Test Description", "Test Body", user);
    fakeArticleRepository.save(article);

    comment = Comment.create("Test Comment", user, article);
    fakecommentRepository.save(comment);
  }

  @Test
  public void should_create_comment_successfully() {
    // given
    NewCommentParam param = new NewCommentParam("New Comment Content");

    // when
    CommentPersistResponse response = commentService.createComment(article.getSlug(), user, param);

    // then
    assertNotNull(response);
    assertEquals(comment.getId(), response.id());
  }

  @Test
  public void should_throw_exception_when_article_not_found_on_comment_creation() {
    // given
    NewCommentParam param = new NewCommentParam("New Comment Content");

    // when & then
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          commentService.createComment("invalid-slug", user, param);
        });
  }

  //	@Test
  public void should_delete_comment_successfully() {
    // when
    UUID commentId = comment.getId();
    commentService.delete(user, commentId);

    // then
    assertThrows(ResourceNotFoundException.class, () -> commentService.delete(user, commentId));
  }

  //	  @Test
  public void should_throw_exception_when_deleting_unauthorized_comment() {
    // when & then
    assertThrows(
        NoAuthorizationException.class,
        () -> {
          commentService.delete(anotherUser, comment.getId());
        });
  }

  @Test
  public void should_throw_exception_when_comment_not_found_on_delete() {
    // given

    // when & then
    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          commentService.delete(user, UUID.nameUUIDFromBytes("invalid-id".getBytes()));
        });
  }

  //	  @Test
  public void should_update_comment_successfully() {
    // given
    NewCommentParam param = new NewCommentParam("Updated Comment Content");

    // when
    commentService.update(comment.getId(), user, param);

    // then
    assertEquals("Updated Comment Content", comment.getBody());
  }

  //	  @Test
  public void should_throw_exception_when_updating_unauthorized_comment() {
    // given
    User anotherUser = User.of("other@test.com", "otherUser", "password", "", "");
    NewCommentParam param = new NewCommentParam("Updated Comment Content");

    // when & then
    assertThrows(
        NoAuthorizationException.class,
        () -> {
          commentService.update(comment.getId(), anotherUser, param);
        });
  }

  @Test
  public void should_throw_exception_when_comment_not_found_on_update() {
    // given
    NewCommentParam param = new NewCommentParam("Updated Comment Content");
    UUID randomUUID = UUID.randomUUID();

    // when & then
    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          commentService.update(randomUUID, user, param);
        });
  }
}
