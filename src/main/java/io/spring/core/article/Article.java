package io.spring.core.article;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import java.util.List;

import io.spring.core.BaseTimeEntity;
import io.spring.core.comment.Comment;
import io.spring.core.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
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

  @Column(nullable = false)
  @OneToMany(mappedBy = "article", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  private List<Comment> comments;

  @Column(nullable = false)
  @OneToMany(mappedBy = "article", cascade = ALL, orphanRemoval = true, fetch = LAZY)
  private List<Tag> tags;

  public static Article create(
      String title, String description, String body, User author) {
    return Article.builder()
        .title(title)
        .slug(toSlug(title))
        .description(description)
        .body(body)
        .author(author)
        .build();
  }

  public void update(String title, String description, String body) {
    if (!title.isEmpty()) {
      this.title = title;
      this.slug = toSlug(title);
    }
    if (!title.isEmpty()) {
      this.description = description;
    }
    if (!title.isEmpty()) {
      this.body = body;
    }
  }

  public void addTag(Tag tag) {
    tags.add(tag);
  }
  public static String toSlug(String title) {
    return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
  }
}
