package io.spring.application;

import io.spring.application.data.CommentData;
import io.spring.core.user.User;
import io.spring.infrastructure.mybatis.readservice.CommentReadService;
import io.spring.infrastructure.mybatis.readservice.UserRelationshipQueryService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentQueryService {
  private CommentReadService commentReadService;
  private UserRelationshipQueryService userRelationshipQueryService;

  public Optional<CommentData> findById(String id, User user) {
    CommentData commentData = commentReadService.findById(id);
    if (commentData == null) {
      return Optional.empty();
    } else {
      commentData
          .profileData()
          .updateFollowing(
              userRelationshipQueryService.isUserFollowing(
                  user.getId(), commentData.profileData().getId()));
    }
    return Optional.ofNullable(commentData);
  }

  public List<CommentData> findByArticleId(String articleId, User user) {
    List<CommentData> comments = commentReadService.findByArticleId(articleId);
    if (comments.size() > 0 && user != null) {
      Set<String> followingAuthors =
          userRelationshipQueryService.followingAuthors(
              user.getId(),
              comments.stream()
                  .map(commentData -> commentData.profileData().getId())
                  .collect(Collectors.toList()));
      comments.forEach(
          commentData -> {
            if (followingAuthors.contains(commentData.profileData().getId())) {
              commentData.profileData().updateFollowing(true);
            }
          });
    }
    return comments;
  }

  public CursorPager<CommentData> findByArticleIdWithCursor(
      String articleId, User user, CursorPageParameter<DateTime> page) {
    List<CommentData> comments = commentReadService.findByArticleIdWithCursor(articleId, page);
    if (comments.isEmpty()) {
      return new CursorPager<>(new ArrayList<>(), page.getDirection(), false);
    }
    if (user != null) {
      Set<String> followingAuthors =
          userRelationshipQueryService.followingAuthors(
              user.getId(),
              comments.stream()
                  .map(commentData -> commentData.profileData().getId())
                  .collect(Collectors.toList()));
      comments.forEach(
          commentData -> {
            if (followingAuthors.contains(commentData.profileData().getId())) {
              commentData.profileData().updateFollowing(true);
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
}
