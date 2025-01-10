package io.spring.api.comment.request;

import jakarta.validation.constraints.NotBlank;

public record NewCommentParam(@NotBlank(message = "can't be empty") String body) {}
