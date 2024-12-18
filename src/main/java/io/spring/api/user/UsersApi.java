package io.spring.api.user;

import static org.springframework.http.HttpStatus.CREATED;

import io.spring.api.exception.InvalidAuthenticationException;
import io.spring.api.user.request.LoginParam;
import io.spring.api.user.request.RegisterParam;
import io.spring.api.user.request.UpdateUserCommand;
import io.spring.api.user.request.UpdateUserParam;
import io.spring.api.user.response.UserData;
import io.spring.api.user.response.UserPersistResponse;
import io.spring.api.user.response.UserResponse;
import io.spring.api.user.response.UserWithToken;
import io.spring.application.UserQueryService;
import io.spring.application.user.UserService;
import io.spring.core.user.User;
import io.spring.infrastructure.service.JwtService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersApi {
  private UserService userService;

  @PostMapping
  public ResponseEntity<UserPersistResponse> createUser(@Valid @RequestBody RegisterParam registerParam) {
    return ResponseEntity.status(CREATED).body(userService.createUser(registerParam));
  }

  @PostMapping(path = "/login")
  public ResponseEntity<UserWithToken> userLogin(@Valid @RequestBody LoginParam loginParam) {
    return ResponseEntity.ok(userService.login(loginParam));
  }

  @GetMapping("/mypage")
  public ResponseEntity<UserResponse> currentUser(
      @AuthenticationPrincipal User currentUser) {
    return ResponseEntity.ok(userService.getUserInfo(currentUser.getId()));
  }

  @PatchMapping("/mypage")
  public ResponseEntity<Void> updateProfile(
      @AuthenticationPrincipal User currentUser,
      @Valid @RequestBody UpdateUserParam updateUserParam) {

    userService.updateUser(new UpdateUserCommand(currentUser, updateUserParam));
    return ResponseEntity.noContent().build();
  }
}
