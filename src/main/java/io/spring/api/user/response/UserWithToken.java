package io.spring.api.user.response;

public record UserWithToken(String email, String username, String bio, String image, String token) {
  public static UserWithToken of(UserData userData, String token) {
    return new UserWithToken(
        userData.getEmail(), userData.getUsername(), userData.getBio(), userData.getImage(), token);
  }
}
