package io.spring.application.data;

import io.spring.application.PageCursor;

public sealed interface Node permits ArticleData, CommentData {
  PageCursor getCursor();
}
