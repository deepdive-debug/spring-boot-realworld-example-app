package io.spring.api.data;

import io.spring.application.PageCursor;

public sealed interface Node permits ArticleData, CommentData {
  PageCursor getCursor();
}
