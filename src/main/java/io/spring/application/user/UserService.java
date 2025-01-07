package io.spring.application.user;

import io.spring.api.exception.InvalidAuthenticationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.api.user.request.LoginParam;
import io.spring.api.user.request.RegisterParam;
import io.spring.api.user.request.UpdateUserCommand;
import io.spring.api.user.request.UpdateUserParam;
import io.spring.api.user.response.UserPersistResponse;
import io.spring.api.user.response.UserResponse;
import io.spring.api.user.response.UserWithToken;
import io.spring.core.user.domain.User;
import io.spring.core.user.domain.UserRepository;
import io.spring.infrastructure.service.JwtService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final UserValidator userValidator;

  @Value("${image.default}")
  private String defaultImage;

  @Transactional
  public UserPersistResponse createUser(@Valid RegisterParam registerParam) {
    userValidator.validateRegistration(registerParam.email(), registerParam.username());

    User user =
        User.of(
            registerParam.email(),
            registerParam.username(),
            passwordEncoder.encode(registerParam.password()),
            "",
            defaultImage);
    userRepository.save(user);
    return UserPersistResponse.from(user);
  }

  public UserWithToken login(LoginParam loginParam) {
    User user =
        userRepository.findByEmail(loginParam.email()).orElseThrow(ResourceNotFoundException::new);

    if (passwordEncoder.matches(loginParam.password(), user.getPassword())) {
      return UserWithToken.of(user, jwtService.toToken(user));

    } else {
      throw new InvalidAuthenticationException();
    }
  }

  @Transactional
  public void updateUser(@Valid UpdateUserCommand command) {
    User user = command.targetUser();
    UpdateUserParam updateUserParam = command.param();

    userValidator.validateEmailAvailability(updateUserParam.email(), user);
    userValidator.validateUsernameAvailability(updateUserParam.username(), user);

    user.update(
        updateUserParam.email(),
        updateUserParam.username(),
        updateUserParam.password(),
        updateUserParam.bio(),
        updateUserParam.image());

    userRepository.save(user);
  }

  public User findByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(ResourceNotFoundException::new);
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(ResourceNotFoundException::new);
  }

  // public void saveRelation(FollowRelation followRelation) {
  //   userRepository.saveRelation(followRelation);
  // }
  //
  // public FollowRelation findRelation(String userId, String targetId) {
  //   Object ResourceNotFoundException;
  //   return userRepository
  //       .findRelation(userId, targetId)
  //       .orElseThrow(ResourceNotFoundException::new);
  // }
  //
  // public void removeRelation(FollowRelation relation) {
  //   userRepository.removeRelation(relation);
  // }

  public UserResponse getUserInfo(UUID id) {
    User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    return UserResponse.of(user);
  }
}
