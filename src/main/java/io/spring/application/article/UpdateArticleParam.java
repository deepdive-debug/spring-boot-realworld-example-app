package io.spring.application.article;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("article")
public record UpdateArticleParam(String title, String body, String description) {}
