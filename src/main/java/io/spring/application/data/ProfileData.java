package io.spring.application.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileData {
  @JsonIgnore private String id;
  private String username;
  private String bio;
  private String image;
  private boolean following;

  @Builder(access = AccessLevel.PRIVATE)
  private ProfileData(String id, String username, String bio, String image, boolean following) {
    this.id = id;
    this.username = username;
    this.bio = bio;
    this.image = image;
    this.following = following;
  }

  public static ProfileData of(
      String id, String username, String bio, String image, boolean following) {
    return ProfileData.builder()
        .id(id)
        .username(username)
        .bio(bio)
        .image(image)
        .following(following)
        .build();
  }

  public void updateFollowing(boolean following) {
    this.following = following;
  }
}
