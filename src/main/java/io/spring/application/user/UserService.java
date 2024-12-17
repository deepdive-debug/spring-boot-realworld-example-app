package io.spring.application.user;

import io.spring.api.exception.InvalidAuthenticationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Optional;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
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
    User user = command.getTargetUser();
    UpdateUserParam updateUserParam = command.getParam();
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
	  return userRepository.findRelation(userId, targetId).orElseThrow(ResourceNotFoundException::new);
  }

  public void removeRelation(FollowRelation relation) {
    userRepository.removeRelation(relation);
  }

}

@Constraint(validatedBy = UpdateUserValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@interface UpdateUserConstraint {

  String message() default "invalid update param";

  Class[] groups() default {};

  Class[] payload() default {};
}

class UpdateUserValidator implements ConstraintValidator<UpdateUserConstraint, UpdateUserCommand> {

  @Autowired private UserRepository userRepository;

  @Override
  public boolean isValid(UpdateUserCommand value, ConstraintValidatorContext context) {
    String inputEmail = value.getParam().email();
    String inputUsername = value.getParam().username();
    final User targetUser = value.getTargetUser();

    boolean isEmailValid =
        userRepository.findByEmail(inputEmail).map(user -> user.equals(targetUser)).orElse(true);
    boolean isUsernameValid =
        userRepository
            .findByUsername(inputUsername)
            .map(user -> user.equals(targetUser))
            .orElse(true);
    if (isEmailValid && isUsernameValid) {
      return true;
    } else {
      context.disableDefaultConstraintViolation();
      if (!isEmailValid) {
        context
            .buildConstraintViolationWithTemplate("email already exist")
            .addPropertyNode("email")
            .addConstraintViolation();
      }
      if (!isUsernameValid) {
        context
            .buildConstraintViolationWithTemplate("username already exist")
            .addPropertyNode("username")
            .addConstraintViolation();
      }
      return false;
    }
  }
}
