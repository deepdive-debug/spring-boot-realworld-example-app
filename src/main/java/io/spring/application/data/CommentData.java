package io.spring.application.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.spring.application.DateTimeCursor;
import io.spring.application.Node;
import org.joda.time.DateTime;

public record CommentData(
    String id,
    String body,
    @JsonIgnore String articleId,
    DateTime createdAt,
    DateTime updatedAt,
    @JsonProperty("author") ProfileData profileData)
    implements Node {
  @Override
  public DateTimeCursor getCursor() {
    return new DateTimeCursor(createdAt);
  }
}
