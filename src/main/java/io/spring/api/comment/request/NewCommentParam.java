package io.spring.api.comment.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;

@JsonRootName("comment")
public record NewCommentParam(@NotBlank(message = "can't be empty") String body) {}
