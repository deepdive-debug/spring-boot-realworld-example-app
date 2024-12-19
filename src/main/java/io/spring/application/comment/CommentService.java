package io.spring.application.comment;

import io.spring.api.comment.request.NewCommentParam;
import io.spring.api.comment.response.CommentPersistResponse;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.article.Article;
import io.spring.core.article.ArticleRepository;
import io.spring.core.comment.Comment;
import io.spring.core.comment.CommentRepository;
import io.spring.core.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
  private final CommentRepository commentRepository;
  private final ArticleRepository articleRepository;

  @Transactional
  public CommentPersistResponse createComment(
      String slug, User user, NewCommentParam newCommentParam) {
    Article article =
        articleRepository
            .findBySlug(slug)
            .orElseThrow(() -> new IllegalArgumentException("Article not found"));

    Comment comment = Comment.create(newCommentParam.body(), user, article);
    commentRepository.save(comment);

    return CommentPersistResponse.from(comment);
  }

  @Transactional
  public void delete(User user, String commentId) {
    Comment comment = findComment(commentId);
    isAuthor(user, comment);
    commentRepository.delete(comment);
  }

  @Transactional
  public void update(String commentId, User user, NewCommentParam newCommentParam) {
    Comment comment = findComment(commentId);
    isAuthor(user, comment);
    comment.update(newCommentParam.body());
  }

  private Comment findComment(String commentId) {
    return commentRepository.findById(commentId).orElseThrow(ResourceNotFoundException::new);
  }

  private void isAuthor(User user, Comment comment) {
    if (!user.getId().equals(comment.getCommenter().getId())) {
      throw new NoAuthorizationException();
    }
  }
}
