package io.spring.core.article;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "name")
public class Tag {
  private String id;
  private String name;

  @Builder(access = AccessLevel.PRIVATE)
  private Tag(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public static Tag of(String name) {
    return Tag.builder()
        .id(UUID.randomUUID().toString())
        .name(name)
        .build();
  }
}
