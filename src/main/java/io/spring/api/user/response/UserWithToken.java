package io.spring.api.user.response;

import io.spring.core.user.User;

public record UserWithToken(String email, String username, String bio, String image, String token) {
  public static UserWithToken of(User user, String token) {
    return new UserWithToken(
        user.getEmail(), user.getUsername(), user.getBio(), user.getImage(), token);
  }
}
