package io.spring.core.article.infrastructure;

import io.spring.core.article.domain.Tag;
import io.spring.core.article.domain.TagRepository;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepository {
  private final JpaTagRepository jpaTagRepository;

  @Override
  public void saveAll(List<Tag> tags) {
    jpaTagRepository.saveAll(tags);
  }

  @Override
  public Collection<Tag> findAll() {
    return jpaTagRepository.findAll();
  }
}
