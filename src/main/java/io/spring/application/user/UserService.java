package io.spring.application.user;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.api.user.request.RegisterParam;
import io.spring.api.user.request.UpdateUserCommand;
import io.spring.api.user.request.UpdateUserParam;
import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UserService {
  private UserRepository userRepository;
  private String defaultImage;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(
      UserRepository userRepository,
      @Value("${image.default}") String defaultImage,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.defaultImage = defaultImage;
    this.passwordEncoder = passwordEncoder;
  }

  public User createUser(@Valid RegisterParam registerParam) {
    User user =
        User.of(
            registerParam.email(),
            registerParam.username(),
            passwordEncoder.encode(registerParam.password()),
            "",
            defaultImage);
    userRepository.save(user);
    return user;
  }

  public void updateUser(@Valid UpdateUserCommand command) {
    User user = command.targetUser();
    UpdateUserParam updateUserParam = command.param();
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

  public void saveRelation(FollowRelation followRelation) {
    userRepository.saveRelation(followRelation);
  }

  public FollowRelation findRelation(String userId, String targetId) {
	  Object ResourceNotFoundException;
	  return userRepository.findRelation(userId, targetId).orElseThrow(ResourceNotFoundException::new);
  }

  public void removeRelation(FollowRelation relation) {
    userRepository.removeRelation(relation);
  }

}
