package io.spring.core.article.infrastructure;

import io.spring.core.article.domain.Tag;
import io.spring.core.article.domain.TagRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FakeTagRepository implements TagRepository {
  private final List<Tag> data = Collections.synchronizedList(new ArrayList<>());

  @Override
  public void saveAll(List<Tag> tags) {
    for (Tag tag : tags) {
      Tag newTag = Tag.builder()
          .name(tag.getName()).article(tag.getArticle())
          .build();
      newTag.initId();
      data.add(newTag);
    }
  }

  @Override
  public Collection<Tag> findAll() {
    return data;
  }
}
