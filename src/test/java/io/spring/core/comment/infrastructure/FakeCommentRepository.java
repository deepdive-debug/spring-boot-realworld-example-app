package io.spring.core.comment.infrastructure;

import io.spring.core.comment.domain.Comment;
import io.spring.core.comment.domain.CommentRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FakeCommentRepository implements CommentRepository {
  private final List<Comment> data = Collections.synchronizedList(new ArrayList<>());

  @Override
  public Comment save(Comment comment) {
    Comment newComment =
        Comment.builder()
            .body(comment.getBody())
            .commenter(comment.getCommenter())
            .article(comment.getArticle())
            .build();

    data.add(newComment);
    return newComment;
  }

  @Override
  public void delete(Comment comment) {
    UUID id = comment.getId();
    Optional<Comment> foundComment = data.stream().filter(c -> c.getId().equals(id)).findFirst();
    foundComment.ifPresent(data::remove);
  }

  @Override
  public Optional<Comment> findById(UUID commentId) {
    return data.stream().filter(c -> c.getId().equals(commentId)).findFirst();
  }
}
