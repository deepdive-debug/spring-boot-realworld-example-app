package io.spring.api.article.response.tag;

import io.spring.core.article.Tag;

public record TagResponse(String name) {
  public static TagResponse from(Tag tag) {
    return new TagResponse(tag.getName());
  }
}
