package io.spring.core.comment.infrastructure;

import io.spring.core.comment.domain.Comment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentRepository extends JpaRepository<Comment, UUID> {}
