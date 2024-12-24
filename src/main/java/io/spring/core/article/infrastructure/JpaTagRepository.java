package io.spring.core.article.infrastructure;

import io.spring.core.article.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTagRepository extends JpaRepository<Tag, String> {}
