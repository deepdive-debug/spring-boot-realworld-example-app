// package io.spring.infrastructure.repository;
//
// import io.spring.core.comment.domain.Comment;
// import io.spring.core.comment.domain.CommentRepository;
// import io.spring.infrastructure.mybatis.mapper.CommentMapper;
// import java.util.Optional;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
//
// @Component
// public class MyBatisCommentRepository implements CommentRepository {
//   private CommentMapper commentMapper;
//
//   @Autowired
//   public MyBatisCommentRepository(CommentMapper commentMapper) {
//     this.commentMapper = commentMapper;
//   }
//
//   @Override
//   public void save(Comment comment) {
//     commentMapper.insert(comment);
//   }
//
//   @Override
//   public Optional<Comment> findById(String articleId, String id) {
//     return Optional.ofNullable(commentMapper.findById(articleId, id));
//   }
//
//   @Override
//   public void remove(Comment comment) {
//     commentMapper.delete(comment.getId());
//   }
// }
