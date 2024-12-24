package io.spring.core.comment.domain;

import java.util.Optional;

public interface CommentRepository {

  Comment save(Comment comment);

  void delete(Comment comment);

  Optional<Comment> findById(String commentId);
}
