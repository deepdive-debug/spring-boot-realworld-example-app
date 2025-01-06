package io.spring.application;

import io.spring.api.article.response.tag.TagResponse;
import io.spring.core.article.domain.Article;
import io.spring.core.article.domain.Tag;
import io.spring.core.article.domain.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class TagsQueryServiceTest {

	@Mock
	private TagRepository tagRepository;

	private TagsQueryService tagsQueryService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		tagsQueryService = new TagsQueryService(tagRepository);
	}

	@Test
	void shouldReturnAllTags() {
		// Given
		Article article = Article.builder().title("Test Article").build();
		Tag tag1 = Tag.create("Tag1", article);
		Tag tag2 = Tag.create("Tag2", article);
		List<Tag> tags = List.of(tag1, tag2);

		when(tagRepository.findAll()).thenReturn(tags);

		// When
		List<TagResponse> tagResponses = tagsQueryService.allTags();

		// Then
		assertThat(tagResponses).hasSize(2);
		assertThat(tagResponses).extracting(TagResponse::name).containsExactly("Tag1", "Tag2");
	}
}
