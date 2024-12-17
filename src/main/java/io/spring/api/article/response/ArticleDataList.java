package io.spring.api.article.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

import io.spring.api.data.ArticleData;

public record ArticleDataList(
    @JsonProperty("articles") List<ArticleData> articleDatas,
    @JsonProperty("articlesCount") int count) {}
