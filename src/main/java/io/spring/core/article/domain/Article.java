package io.spring.core.article.domain;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import io.spring.core.BaseTimeEntity;
import io.spring.core.comment.domain.Comment;
import io.spring.core.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "article",
    indexes = {@Index(name = "idx_article_created_at", columnList = "created_at")})
public class Article extends BaseTimeEntity {

  @JoinColumn(name = "author_id", nullable = false)
  @ManyToOne(fetch = LAZY)
  private User author;

  @Column(nullable = false, unique = true)
  private String slug;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String body;

  @OneToMany(mappedBy = "article", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  @Builder.Default
  private List<Comment> comments = new ArrayList<>();

  @OneToMany(mappedBy = "article", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  @Builder.Default
  private List<Tag> tags = new ArrayList<>();

  public static Article create(String title, String description, String body, User author) {
    return Article.builder()
        .title(title)
        .slug(toSlug(title))
        .description(description)
        .body(body)
        .author(author)
        .build();
  }

  public void update(String title, String description, String body) {
    if (title != null && !title.isEmpty()) {
      this.title = title;
      this.slug = toSlug(title);
    }
    if (description != null && !description.isEmpty()) {
      this.description = description;
    }
    if (body != null && !body.isEmpty()) {
      this.body = body;
    }
  }

  public static String toSlug(String title) {
    return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
  }
}
