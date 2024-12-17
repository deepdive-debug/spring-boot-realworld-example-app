package io.spring.api.user.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
  private String id;
  private String email;
  private String username;
  private String bio;
  private String image;
}
