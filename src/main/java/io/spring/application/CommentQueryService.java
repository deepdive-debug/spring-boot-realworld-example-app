package io.spring.application;

import io.spring.api.comment.request.NewCommentParam;
import io.spring.api.data.CommentData;
import io.spring.api.exception.NoAuthorizationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.api.user.response.ProfileData;
import io.spring.core.article.Article;
import io.spring.core.comment.Comment;
import io.spring.core.comment.CommentRepository;
import io.spring.core.service.AuthorizationService;
import io.spring.core.user.User;
import io.spring.infrastructure.mybatis.readservice.CommentReadService;
import io.spring.infrastructure.mybatis.readservice.UserRelationshipQueryService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentQueryService {
  private CommentReadService commentReadService;
  private UserRelationshipQueryService userRelationshipQueryService;
  private CommentRepository commentRepository;
  private ArticleQueryService articleQueryService;

  public CommentData findById(String id, User user) {
    CommentData commentData = commentReadService.findById(id);
    if (commentData == null) {
      return null;
    }
    ProfileData profileData = commentData.getProfileData();
    profileData.updateFollowing(userRelationshipQueryService.isUserFollowing(user.getId(), profileData.getId()));
    return commentData;
  }

  public List<CommentData> findByArticleId(String articleId, User user) {
    List<CommentData> comments = commentReadService.findByArticleId(articleId);
    if (comments.size() > 0 && user != null) {
      Set<String> followingAuthors =
          userRelationshipQueryService.followingAuthors(
              user.getId(),
              comments.stream()
                  .map(commentData -> commentData.getProfileData().getId())
                  .collect(Collectors.toList()));
      comments.forEach(
          commentData -> {
            if (followingAuthors.contains(commentData.getProfileData().getId())) {
              commentData.getProfileData().updateFollowing(true);
            }
          });
    }
    return comments;
  }

  public CursorPager<CommentData> findByArticleIdWithCursor(
      String articleId, User user, CursorPageParameter<Instant> page) {
    List<CommentData> comments = commentReadService.findByArticleIdWithCursor(articleId, page);
    if (comments.isEmpty()) {
      return new CursorPager<>(new ArrayList<>(), page.getDirection(), false);
    }
    if (user != null) {
      Set<String> followingAuthors =
          userRelationshipQueryService.followingAuthors(
              user.getId(),
              comments.stream()
                  .map(commentData -> commentData.getProfileData().getId())
                  .collect(Collectors.toList()));
      comments.forEach(
          commentData -> {
            if (followingAuthors.contains(commentData.getProfileData().getId())) {
              commentData.getProfileData().updateFollowing(true);
            }
          });
    }
    boolean hasExtra = comments.size() > page.getLimit();
    if (hasExtra) {
      comments.remove(page.getLimit());
    }
    if (!page.isNext()) {
      Collections.reverse(comments);
    }
    return new CursorPager<>(comments, page.getDirection(), hasExtra);
  }

  // 추가
  public CommentData save(String slug, User user, NewCommentParam newCommentParam) {
    Article article = articleQueryService.findBySlug(slug);
    Comment comment = Comment.of(newCommentParam.body(), user.getId(), article.getId());
    commentRepository.save(comment);
    return findById(comment.getId(), user);
  }

  public List<CommentData> findCommentsBySlug(String slug, User user) {
    Article article = articleQueryService.findBySlug(slug);
    return findByArticleId(article.getId(), user);
  }

  public Comment findCommentById(String articleId, String commentId) {
    return commentRepository
        .findById(articleId, commentId)
        .orElseThrow(ResourceNotFoundException::new);
  }

  public void remove(String slug, String commentId, User user) {
    Article article = articleQueryService.findBySlug(slug);
    Comment comment = findCommentById(article.getId(), commentId);
    if (!AuthorizationService.canWriteComment(user, article, comment)) {
      throw new NoAuthorizationException();
    }
    commentRepository.remove(comment);

  }
}
