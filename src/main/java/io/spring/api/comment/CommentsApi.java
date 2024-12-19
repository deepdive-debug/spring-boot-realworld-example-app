package io.spring.api.comment;

import static org.springframework.http.HttpStatus.CREATED;

import io.spring.api.comment.request.NewCommentParam;
import io.spring.api.comment.response.CommentPersistResponse;
import io.spring.application.comment.CommentService;
import io.spring.core.user.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/articles/{slug}/comments")
@AllArgsConstructor
public class CommentsApi {

  private CommentService commentService;

  @PostMapping
  public ResponseEntity<CommentPersistResponse> createComment(
      @PathVariable("slug") String slug,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody NewCommentParam newCommentParam) {
    return ResponseEntity.status(CREATED)
        .body(commentService.createComment(slug, user, newCommentParam));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Void> updateComment(
      @PathVariable("id") String commentId,
      @AuthenticationPrincipal User user,
      @Valid @RequestBody NewCommentParam newCommentParam) {
    commentService.update(commentId, user, newCommentParam);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteComment(
      @PathVariable("id") String commentId, @AuthenticationPrincipal User user) {
    commentService.delete(user, commentId);
    return ResponseEntity.noContent().build();
  }
}
