package io.spring.core.service;

import io.spring.core.article.Article;
import io.spring.core.comment.Comment;
import io.spring.core.user.User;

public class AuthorizationService {
  public static boolean canWriteArticle(User user, Article article) {
    return user.getId().equals(article.getAuthor().getId());
  }

  public static boolean canWriteComment(User user, Article article, Comment comment) {
    return user.getId().equals(article.getAuthor().getId())
        || user.getId().equals(comment.getCommenter().getId());
  }
}
