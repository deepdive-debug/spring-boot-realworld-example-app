package io.spring.application;

import io.spring.api.article.response.tag.TagResponse;
import io.spring.core.article.domain.TagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagsQueryService {
  private final TagRepository tagRepository;

  public List<TagResponse> allTags() {
    return tagRepository.findAll().stream().map(TagResponse::from).toList();
  }
}
