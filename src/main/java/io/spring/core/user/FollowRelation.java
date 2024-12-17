package io.spring.core.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FollowRelation {
  private String userId;
  private String targetId;

  @Builder(access = AccessLevel.PRIVATE)
  private FollowRelation(String userId, String targetId) {
    this.userId = userId;
    this.targetId = targetId;
  }

  public static FollowRelation of(String userId, String targetId) {
    return FollowRelation
        .builder()
        .userId(userId)
        .targetId(targetId)
        .build();
  }
}
