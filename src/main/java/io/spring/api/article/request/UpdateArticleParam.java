package io.spring.api.article.request;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("article")
public record UpdateArticleParam(String title, String body, String description) {}
