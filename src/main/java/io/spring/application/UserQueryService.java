package io.spring.application;

import io.spring.api.exception.InvalidAuthenticationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.api.user.request.LoginParam;
import io.spring.api.user.request.RegisterParam;
import io.spring.api.user.request.UpdateUserCommand;
import io.spring.api.user.request.UpdateUserParam;
import io.spring.api.user.response.UserData;
import io.spring.api.user.response.UserWithToken;
import io.spring.application.user.UserService;
import io.spring.core.user.User;
import io.spring.infrastructure.mybatis.readservice.UserReadService;
import java.util.Optional;

import io.spring.infrastructure.service.JwtService;
import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserQueryService {
  private UserReadService userReadService;
  private UserService userService;
  private JwtService jwtService;
  private PasswordEncoder passwordEncoder;

  public UserData findById(String id) {
    return Optional.ofNullable(userReadService.findById(id))
        .orElseThrow(ResourceNotFoundException::new);
  }

  public UserData updateProfile(User currentUser, UpdateUserParam updateUserParam) {
    userService.updateUser(new UpdateUserCommand(currentUser, updateUserParam));
    return findById(currentUser.getId());
  }

  public UserWithToken createUser(RegisterParam registerParam) {
    User user = userService.createUser(registerParam);
    UserData userData = findById(user.getId());
    return UserWithToken.of(userData, jwtService.toToken(user));
  }

  public UserWithToken login(LoginParam loginParam) {
    User user = userService.findByEmail(loginParam.email());
    if (passwordEncoder.matches(loginParam.password(), user.getPassword())) {
      UserData userData = findById(user.getId());
      return UserWithToken.of(userData, jwtService.toToken(user));
    }
    throw new InvalidAuthenticationException();
  }

  public UserWithToken findById(User currentUser, String authorization) {
    UserData userData = findById(currentUser.getId());
    return UserWithToken.of(userData, authorization.split(" ")[1]);
  }

  public UserWithToken update(User currentUser, UpdateUserParam updateUserParam, String token) {
    UserData userData = updateProfile(currentUser, updateUserParam);
    return UserWithToken.of(userData, token.split(" ")[1]);
  }
}
