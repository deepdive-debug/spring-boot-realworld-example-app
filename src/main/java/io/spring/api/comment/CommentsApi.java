package io.spring.api.comment;

import io.spring.api.comment.request.NewCommentParam;
import io.spring.api.data.CommentData;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.application.ArticleQueryService;
import io.spring.application.CommentQueryService;
import io.spring.core.article.Article;
import io.spring.core.comment.Comment;
import io.spring.core.service.AuthorizationService;
import io.spring.core.user.User;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/articles/{slug}/comments")
@AllArgsConstructor
public class CommentsApi {
  private CommentQueryService commentQueryService;
  private ArticleQueryService articleQueryService;

  @PostMapping
  public ResponseEntity<?> createComment(
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody NewCommentParam newCommentParam) {
    Article article = articleQueryService.findBySlug(slug);
    Comment comment = Comment.of(newCommentParam.body(), user.getId(), article.getId());
    commentQueryService.save(comment);
    return ResponseEntity.status(201)
        .body(commentResponse(commentQueryService.findById(comment.getId(), user).get()));
  }

  @GetMapping
  public ResponseEntity getComments(
      @PathVariable("slug") String slug, @AuthenticationPrincipal User user) {
    Article article = articleQueryService.findBySlug(slug);
    List<CommentData> comments = commentQueryService.findByArticleId(article.getId(), user);
    return ResponseEntity.ok(
        new HashMap<String, Object>() {
          {
            put("comments", comments);
          }
        });
  }

  @DeleteMapping(path = "/{id}")
  public ResponseEntity deleteComment(
      @PathVariable("slug") String slug,
      @PathVariable("id") String commentId,
      @AuthenticationPrincipal User user) {
    Article article = articleQueryService.findBySlug(slug);

    Comment comment = commentQueryService.findCommentById(article.getId(), commentId);
    if (!AuthorizationService.canWriteComment(user, article, comment)) {
      throw new NoAuthorizationException();
    }
    commentQueryService.remove(comment);
    return ResponseEntity.noContent().build();
  }

  private Map<String, Object> commentResponse(CommentData commentData) {
    return new HashMap<String, Object>() {
      {
        put("comment", commentData);
      }
    };
  }
}
