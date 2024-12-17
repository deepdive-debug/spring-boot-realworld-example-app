package io.spring.api.article.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.spring.api.data.ArticleData;
import java.util.List;

public record ArticleDataList(
    @JsonProperty("articles") List<ArticleData> articleDatas,
    @JsonProperty("articlesCount") int count) {}
