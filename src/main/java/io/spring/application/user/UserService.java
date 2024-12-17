package io.spring.application.user;

import io.spring.api.user.request.RegisterParam;
import io.spring.api.user.request.UpdateUserCommand;
import io.spring.api.user.request.UpdateUserParam;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import javax.validation.Valid;
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
        new User(
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
}
