package io.spring.api.user.response;

import io.spring.core.user.domain.User;
import java.util.UUID;

public record UserWithToken(
    UUID id, String email, String username, String bio, String image, String token) {
  public static UserWithToken of(User user, String token) {
    return new UserWithToken(
        user.getId(), user.getEmail(), user.getUsername(), user.getBio(), user.getImage(), token);
  }
}
