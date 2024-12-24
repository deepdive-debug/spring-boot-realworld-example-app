package io.spring.api.user.response;

import io.spring.core.user.domain.User;

public record UserResponse(String id, String username, String bio, String image) {
  public static UserResponse of(User user) {
    return new UserResponse(user.getId(), user.getUsername(), user.getBio(), user.getImage());
  }
}
