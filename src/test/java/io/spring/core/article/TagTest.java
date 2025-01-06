package io.spring.core.article;

import io.spring.core.article.domain.Tag;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TagTest {
	@Test
	public void testCreateTest_success() {
		//given
		Tag tag = Tag.create("test", null);

		//then
		Assertions.assertNotNull(tag);
		Assertions.assertEquals("test", tag.getName());
	}
}
