package io.spring.core.article.domain;

import java.util.Collection;
import java.util.List;

public interface TagRepository {
  void saveAll(List<Tag> tags);

  Collection<Tag> findAll();
}
