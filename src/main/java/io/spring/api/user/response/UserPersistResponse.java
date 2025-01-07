package io.spring.api.user.response;

import io.spring.core.user.domain.User;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserPersistResponse(UUID id) {
  public static UserPersistResponse from(User user) {
    return UserPersistResponse.builder().id(user.getId()).build();
  }
}
