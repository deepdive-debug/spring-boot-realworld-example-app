package io.spring.core.article.infrastructure;

import io.spring.core.article.domain.Tag;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTagRepository extends JpaRepository<Tag, UUID> {}
