package io.spring.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.spring.api.article.response.tag.TagResponse;
import io.spring.core.article.domain.Article;
import io.spring.core.article.domain.Tag;
import io.spring.core.article.domain.TagRepository;
import java.util.List;

import io.spring.core.article.infrastructure.FakeTagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TagsQueryServiceTest {
  private TagRepository tagRepository;
  private TagsQueryService tagsQueryService;

  @BeforeEach
  void setUp() {
    this.tagRepository = new FakeTagRepository();
    tagsQueryService = new TagsQueryService(tagRepository);
  }

  @Test
  void shouldReturnAllTags() {
    // Given
    Article article = Article.builder().title("Test Article").build();
    Tag tag1 = Tag.create("Tag1", article);
    Tag tag2 = Tag.create("Tag2", article);
    List<Tag> tags = List.of(tag1, tag2);

    // When
    tagRepository.saveAll(tags);
    List<TagResponse> tagResponses = tagsQueryService.allTags();

    // Then
    assertThat(tagResponses).hasSize(2);
    assertThat(tagResponses).extracting(TagResponse::name).containsExactly("Tag1", "Tag2");
  }
}
