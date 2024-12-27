package io.spring.core;

import static org.hibernate.annotations.UuidGenerator.Style.RANDOM;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
  @Id
  @GeneratedValue
  @UuidGenerator(style = RANDOM)
  @Column(columnDefinition = "BINARY(16)")
  private UUID id;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  protected LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(nullable = false)
  protected LocalDateTime updatedAt;

  @Column protected LocalDateTime deletedAt;

  public void delete() {
    deletedAt = LocalDateTime.now();
  }
}
