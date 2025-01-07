// package io.spring.api.user;
//
// import io.spring.api.user.response.ProfileData;
// import io.spring.application.ProfileQueryService;
// import io.spring.core.user.domain.User;
// import java.util.HashMap;
// import lombok.AllArgsConstructor;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// @RestController
// @RequestMapping(path = "/profiles/{username}")
// @AllArgsConstructor
// public class ProfileApi {
//   private ProfileQueryService profileQueryService;
//
//   @GetMapping
//   public ResponseEntity getProfile(
//       @PathVariable("username") String username, @AuthenticationPrincipal User user) {
//     return profileResponse(profileQueryService.findByUsername(username, user));
//   }
//
//   @PostMapping(path = "/follow")
//   public ResponseEntity follow(
//       @PathVariable("username") String username, @AuthenticationPrincipal User user) {
//     return profileResponse(profileQueryService.createFollow(username, user));
//   }
//
//   @DeleteMapping(path = "/follow")
//   public ResponseEntity unfollow(
//       @PathVariable("username") String username, @AuthenticationPrincipal User user) {
//     return profileResponse(profileQueryService.unFollow(username, user));
//   }
//
//   private ResponseEntity profileResponse(ProfileData profile) {
//     return ResponseEntity.ok(
//         new HashMap<String, Object>() {
//           {
//             put("profile", profile);
//           }
//         });
//   }
// }
