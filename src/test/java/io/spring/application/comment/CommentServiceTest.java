package io.spring.application.comment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import io.spring.api.comment.request.NewCommentParam;
import io.spring.api.comment.response.CommentPersistResponse;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.comment.Comment;
import io.spring.core.comment.CommentRepository;
import io.spring.core.user.User;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CommentServiceTest {

  @Mock private CommentRepository commentRepository;

  @Mock private ArticleRepository articleRepository;

  @InjectMocks private CommentService commentService;

  private User user;
  private Article article;
  private Comment comment;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);

    // 테스트 데이터 초기화
    user = User.of("test@test.com", "testUser", "password", "bio", "image");
    article = Article.create("Test Title", "Test Description", "Test Body", user);
    comment = Comment.create("Test Comment", user, article);
  }

  @Test
  public void should_create_comment_successfully() {
    // given
    NewCommentParam param = new NewCommentParam("New Comment Content");

    when(articleRepository.findBySlug(article.getSlug())).thenReturn(Optional.of(article));
    when(commentRepository.save(any(Comment.class))).thenReturn(comment);

    // when
    CommentPersistResponse response = commentService.createComment(article.getSlug(), user, param);

    // then
    assertNotNull(response);
    assertEquals(comment.getId(), response.id());
    verify(commentRepository, times(1)).save(any(Comment.class));
  }

  @Test
  public void should_throw_exception_when_article_not_found_on_comment_creation() {
    // given
    NewCommentParam param = new NewCommentParam("New Comment Content");

    when(articleRepository.findBySlug("invalid-slug")).thenReturn(Optional.empty());

    // when & then
    assertThrows(
        IllegalArgumentException.class,
        () -> {
          commentService.createComment("invalid-slug", user, param);
        });

    verify(commentRepository, never()).save(any(Comment.class));
  }

  //  @Test
  public void should_delete_comment_successfully() {
    // given
    when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

    // when
    commentService.delete(user, comment.getId());

    // then
    verify(commentRepository, times(1)).delete(comment);
  }

  //  @Test
  public void should_throw_exception_when_deleting_unauthorized_comment() {
    // given
    User anotherUser = User.of("other@test.com", "otherUser", "password", "", "");
    when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

    // when & then
    assertThrows(
        NoAuthorizationException.class,
        () -> {
          commentService.delete(anotherUser, comment.getId());
        });

    verify(commentRepository, never()).delete(any(Comment.class));
  }

  @Test
  public void should_throw_exception_when_comment_not_found_on_delete() {
    // given
    when(commentRepository.findById(UUID.nameUUIDFromBytes("invalid-id".getBytes())))
        .thenReturn(Optional.empty());
    // when & then
    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          commentService.delete(user, UUID.nameUUIDFromBytes("invalid-id".getBytes()));
        });

    verify(commentRepository, never()).delete(any(Comment.class));
  }

  //  @Test
  public void should_update_comment_successfully() {
    // given
    NewCommentParam param = new NewCommentParam("Updated Comment Content");
    when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

    // when
    commentService.update(comment.getId(), user, param);

    // then
    assertEquals("Updated Comment Content", comment.getBody());
    verify(commentRepository, times(1)).findById(comment.getId());
  }

  //  @Test
  public void should_throw_exception_when_updating_unauthorized_comment() {
    // given
    User anotherUser = User.of("other@test.com", "otherUser", "password", "", "");
    NewCommentParam param = new NewCommentParam("Updated Comment Content");
    when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

    // when & then
    assertThrows(
        NoAuthorizationException.class,
        () -> {
          commentService.update(comment.getId(), anotherUser, param);
        });

    verify(commentRepository, never()).save(any(Comment.class));
  }

  @Test
  public void should_throw_exception_when_comment_not_found_on_update() {
    // given
    NewCommentParam param = new NewCommentParam("Updated Comment Content");
    UUID randomUUID = UUID.randomUUID();
    when(commentRepository.findById(randomUUID)).thenReturn(Optional.empty());

    // when & then
    assertThrows(
        ResourceNotFoundException.class,
        () -> {
          commentService.update(randomUUID, user, param);
        });

    verify(commentRepository, never()).save(any(Comment.class));
  }
}
