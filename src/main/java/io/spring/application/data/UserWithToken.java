package io.spring.application.data;

public record UserWithToken(String email, String username, String bio, String image, String token) {
  public static UserWithToken of(UserData userData, String token) {
    return new UserWithToken(
        userData.email(), userData.username(), userData.bio(), userData.image(), token);
  }
}
