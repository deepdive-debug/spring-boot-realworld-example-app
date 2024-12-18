package io.spring.application.article;

import org.springframework.stereotype.Service;

import io.spring.core.article.ArticleRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ArticleService {
	private final ArticleRepository articleRepository;
}
