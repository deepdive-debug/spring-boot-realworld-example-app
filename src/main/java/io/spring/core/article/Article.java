package io.spring.core.article;

import static java.util.stream.Collectors.toList;

import io.spring.Util;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Article {
  private String userId;
  private String id;
  private String slug;
  private String title;
  private String description;
  private String body;
  private List<Tag> tags;
  private Instant createdAt;
  private Instant updatedAt;

  @Builder(access = AccessLevel.PRIVATE)
  private Article(
      String id,
      String userId,
      String title,
      String description,
      String body,
      List<Tag> tags,
      Instant createdAt,
      Instant updatedAt) {
    this.userId = userId;
    this.id = UUID.randomUUID().toString();
    this.slug = toSlug(title);
    this.title = title;
    this.description = description;
    this.body = body;
    this.tags = tags;
    this.createdAt = createdAt;
    this.updatedAt = createdAt;
  }

  public static Article of(
      String title, String description, String body, List<String> tagList, String userId) {
    return Article.builder()
        .title(title)
        .description(description)
        .body(body)
        .tags(new HashSet<>(tagList).stream().map(Tag::of).collect(toList()))
        .userId(userId)
        .createdAt(Instant.now())
        .build();
  }

  public static Article of(
      String title,
      String description,
      String body,
      List<String> tagList,
      String userId,
      Instant createdAt) {
    return Article.builder()
        .title(title)
        .description(description)
        .body(body)
        .tags(new HashSet<>(tagList).stream().map(Tag::of).collect(toList()))
        .userId(userId)
        .createdAt(createdAt)
        .build();
  }

  public void update(String title, String description, String body) {
    if (!Util.isEmpty(title)) {
      this.title = title;
      this.slug = toSlug(title);
      this.updatedAt = Instant.now();
    }
    if (!Util.isEmpty(description)) {
      this.description = description;
      this.updatedAt = Instant.now();
    }
    if (!Util.isEmpty(body)) {
      this.body = body;
      this.updatedAt = Instant.now();
    }
  }

  public static String toSlug(String title) {
    return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
  }
}
