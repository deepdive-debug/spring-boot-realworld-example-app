package io.spring.api.article.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.spring.application.article.DuplicatedArticleConstraint;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@JsonRootName("article")
public record NewArticleParam(
    @NotBlank(message = "can't be empty") @DuplicatedArticleConstraint String title,
    @NotBlank(message = "can't be empty") String description,
    @NotBlank(message = "can't be empty") String body,
    List<String> tagList) {
  public static NewArticleParam of(
      String title, String description, String body, List<String> tagList) {
    return new NewArticleParam(title, description, body, tagList);
  }
}
