package io.spring.core.comment.infrastructure;

import io.spring.core.comment.domain.Comment;
import io.spring.core.comment.domain.CommentRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
  private final JpaCommentRepository jpaCommentRepository;

  @Override
  public Comment save(Comment comment) {
    return jpaCommentRepository.save(comment);
  }

  @Override
  public void delete(Comment comment) {
    jpaCommentRepository.delete(comment);
  }

  @Override
  public Optional<Comment> findById(UUID commentId) {
    return jpaCommentRepository.findById(commentId);
  }
}
