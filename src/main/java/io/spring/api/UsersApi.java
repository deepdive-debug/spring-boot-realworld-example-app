package io.spring.api;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.spring.api.exception.InvalidAuthenticationException;
import io.spring.application.UserQueryService;
import io.spring.application.data.UserData;
import io.spring.application.data.UserWithToken;
import io.spring.application.user.RegisterParam;
import io.spring.application.user.UserService;
import io.spring.infrastructure.service.JwtService;
import io.spring.core.user.User;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersApi {
  private UserQueryService userQueryService;
  private PasswordEncoder passwordEncoder;
  private JwtService jwtService;
  private UserService userService;

  @PostMapping
  public ResponseEntity createUser(@Valid @RequestBody RegisterParam registerParam) {
    User user = userService.createUser(registerParam);
    UserData userData = userQueryService.findById(user.getId());
    return ResponseEntity.status(201)
        .body(userResponse(UserWithToken.of(userData, jwtService.toToken(user))));
  }

  @PostMapping(path = "/login")
  public ResponseEntity userLogin(@Valid @RequestBody LoginParam loginParam) {
    User user = userService.findByEmail(loginParam.email());
    if (passwordEncoder.matches(loginParam.password(), user.getPassword())) {
      UserData userData = userQueryService.findById(user.getId());
      return ResponseEntity.ok(
          userResponse(UserWithToken.of(userData, jwtService.toToken(user))));
    } else {
      throw new InvalidAuthenticationException();
    }
  }

  private Map<String, Object> userResponse(UserWithToken userWithToken) {
    return new HashMap<String, Object>() {
      {
        put("user", userWithToken);
      }
    };
  }
}

@JsonRootName("user")
record LoginParam(
    @NotBlank(message = "can't be empty") @Email(message = "should be an email") String email,
    @NotBlank(message = "can't be empty") String password) {}
