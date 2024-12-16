package io.spring.application.article;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.util.List;
import javax.validation.constraints.NotBlank;

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
