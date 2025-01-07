package io.spring.core.comment.domain;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository {

  Comment save(Comment comment);

  void delete(Comment comment);

  Optional<Comment> findById(UUID commentId);
}
