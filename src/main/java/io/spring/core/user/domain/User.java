package io.spring.core.user.domain;

import static jakarta.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

import io.spring.core.BaseTimeEntity;
import io.spring.core.article.domain.Article;
import io.spring.core.comment.domain.Comment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class User extends BaseTimeEntity {

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String bio;

  private String image;

  @OneToMany(mappedBy = "author", cascade = ALL, orphanRemoval = true)
  @Builder.Default
  private List<Article> articles = new ArrayList<>();

  @OneToMany(mappedBy = "commenter", cascade = ALL, orphanRemoval = true)
  @Builder.Default
  private List<Comment> comments = new ArrayList<>();

  public static User of(String email, String username, String password, String bio, String image) {
    return User.builder()
        .email(email)
        .username(username)
        .password(password)
        .bio(bio)
        .image(image)
        .build();
  }

  public void update(String email, String username, String password, String bio, String image) {
    if (!email.isEmpty()) {
      this.email = email;
    }

    if (!email.isEmpty()) {
      this.username = username;
    }

    if (!email.isEmpty()) {
      this.password = password;
    }

    if (!email.isEmpty()) {
      this.bio = bio;
    }

    if (!email.isEmpty()) {
      this.image = image;
    }
  }
}
