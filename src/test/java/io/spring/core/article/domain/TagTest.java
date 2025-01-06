package io.spring.core.article.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class TagTest {
  @Test
  public void testCreateTest_success() {
    // given
    Tag tag = Tag.create("test", null);

    // then
    assertNotNull(tag);
    assertEquals("test", tag.getName());
  }
}
