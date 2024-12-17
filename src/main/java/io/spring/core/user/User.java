package io.spring.core.user;

import io.spring.Util;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {

  private String id;
  private String email;
  private String username;
  private String password;
  private String bio;
  private String image;

  @Builder(access = AccessLevel.PRIVATE)
  private User(String id, String email, String username, String password, String bio, String image) {
    this.id = id;
    this.email = email;
    this.username = username;
    this.password = password;
    this.bio = bio;
    this.image = image;
  }

  public static User of(String email, String username, String password, String bio, String image) {
    return User.builder()
        .id(UUID.randomUUID().toString())
        .email(email)
        .username(username)
        .password(password)
        .bio(bio)
        .image(image)
        .build();
  }

  public void update(String email, String username, String password, String bio, String image) {
    if (!Util.isEmpty(email)) {
      this.email = email;
    }

    if (!Util.isEmpty(username)) {
      this.username = username;
    }

    if (!Util.isEmpty(password)) {
      this.password = password;
    }

    if (!Util.isEmpty(bio)) {
      this.bio = bio;
    }

    if (!Util.isEmpty(image)) {
      this.image = image;
    }
  }
}
