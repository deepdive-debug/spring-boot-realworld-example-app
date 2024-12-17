package io.spring.api;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ProfileQueryService;
import io.spring.application.data.ProfileData;
import io.spring.application.user.UserService;
import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/profiles/{username}")
@AllArgsConstructor
public class ProfileApi {
  private ProfileQueryService profileQueryService;
  private UserService userService;

  @GetMapping
  public ResponseEntity getProfile(
      @PathVariable("username") String username, @AuthenticationPrincipal User user) {
    return profileQueryService
        .findByUsername(username, user)
        .map(this::profileResponse)
        .orElseThrow(ResourceNotFoundException::new);
  }

  @PostMapping(path = "/follow")
  public ResponseEntity follow(
      @PathVariable("username") String username, @AuthenticationPrincipal User user) {
      User target = userService.findByUsername(username);
      FollowRelation followRelation = FollowRelation.of(user.getId(), target.getId());
      userService.saveRelation(followRelation);
      return profileResponse(profileQueryService.findByUsername(username, user).get());
  }

  @DeleteMapping(path = "/follow")
  public ResponseEntity unfollow(
      @PathVariable("username") String username, @AuthenticationPrincipal User user) {
      User target = userService.findByUsername(username);
      FollowRelation relation = userService.findRelation(user.getId(), target.getId());
      userService.removeRelation(relation);
      return profileResponse(profileQueryService.findByUsername(username, user).get());
  }

  private ResponseEntity profileResponse(ProfileData profile) {
    return ResponseEntity.ok(
        new HashMap<String, Object>() {
          {
            put("profile", profile);
          }
        });
  }
}
