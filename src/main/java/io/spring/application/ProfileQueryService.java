package io.spring.application;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.api.user.response.ProfileData;
import io.spring.api.user.response.UserData;
import io.spring.application.user.UserService;
import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import io.spring.infrastructure.mybatis.readservice.UserReadService;
import io.spring.infrastructure.mybatis.readservice.UserRelationshipQueryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProfileQueryService {
  private UserService userService;
  private UserReadService userReadService;
  private UserRelationshipQueryService userRelationshipQueryService;

  public ProfileData findByUsername(String username, User currentUser) {
    UserData userData = userReadService.findByUsername(username);
    if (userData == null) {
      throw new ResourceNotFoundException();
    }

    return new ProfileData(
        userData.getId(),
        userData.getUsername(),
        userData.getBio(),
        userData.getImage(),
        currentUser != null
            && userRelationshipQueryService.isUserFollowing(
            currentUser.getId(), userData.getId()));
  }

  public ProfileData createFollow(String username, User user) {
    User target = userService.findByUsername(username);
    FollowRelation followRelation = FollowRelation.of(user.getId(), target.getId());
    userService.saveRelation(followRelation);
    return findByUsername(username, user);
  }

  public ProfileData unFollow(String username, User user) {
    User target = userService.findByUsername(username);
    FollowRelation relation = userService.findRelation(user.getId(), target.getId());
    userService.removeRelation(relation);
    return findByUsername(username, user);
  }
}
