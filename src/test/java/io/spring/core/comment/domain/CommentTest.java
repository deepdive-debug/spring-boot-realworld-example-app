package io.spring.core.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommentTest {

  @Test
  public void commentCreate_success() {
    // given
    Comment comment = Comment.create("body", null, null);

    // then
    assertThat(comment).isNotNull();
    assertEquals("body", comment.getBody());
  }

  @Test
  public void commentUpdate_success() {
    // given
    Comment comment = Comment.create("body", null, null);

    // when
    comment.update("body updated");

    // then
    assertThat(comment).isNotNull();
    assertEquals("body updated", comment.getBody());
  }
}
