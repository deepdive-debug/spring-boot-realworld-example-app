package io.spring.core.comment;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import io.spring.core.BaseTimeEntity;
import io.spring.core.article.Article;
import io.spring.core.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Comment extends BaseTimeEntity {

  @Column(nullable = false)
  private String body;

  @ManyToOne(fetch = LAZY)
  private User commenter;

  @ManyToOne(fetch = LAZY)
  private Article article;

  public static Comment create(String body, User commenter, Article article) {
    return Comment.builder().body(body).commenter(commenter).article(article).build();
  }

  public void update(String body) {
    this.body = body;
  }
}
