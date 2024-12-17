package io.spring.api.user.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileData {
  @JsonIgnore private String id;
  private String username;
  private String bio;
  private String image;
  private boolean following;

  public void updateFollowing(boolean following) {
    this.following = following;
  }
}
