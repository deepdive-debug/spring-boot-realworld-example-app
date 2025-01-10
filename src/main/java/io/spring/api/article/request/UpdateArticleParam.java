package io.spring.api.article.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateArticleParam(
    @NotBlank(message = "can't be empty") String title,
    @NotBlank(message = "can't be empty") String body,
    @NotBlank(message = "can't be empty") String description) {}
