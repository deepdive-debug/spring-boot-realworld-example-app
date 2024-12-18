package io.spring.api.user;

import io.spring.api.user.request.LoginParam;
import io.spring.api.user.request.RegisterParam;
import io.spring.api.user.response.UserWithToken;
import io.spring.application.UserQueryService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersApi {
  private UserQueryService userQueryService;

  @PostMapping
  public ResponseEntity createUser(@Valid @RequestBody RegisterParam registerParam) {
    return ResponseEntity.status(201)
        .body(userResponse(userQueryService.createUser(registerParam)));
  }

  @PostMapping(path = "/login")
  public ResponseEntity userLogin(@Valid @RequestBody LoginParam loginParam) {
    return ResponseEntity.ok(userResponse(userQueryService.login(loginParam)));
  }

  private Map<String, Object> userResponse(UserWithToken userWithToken) {
    return new HashMap<String, Object>() {
      {
        put("user", userWithToken);
      }
    };
  }
}
