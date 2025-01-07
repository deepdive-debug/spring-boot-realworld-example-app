package io.spring.api.user.validation;

import io.spring.api.user.request.UpdateUserCommand;
import io.spring.core.constant.ErrorMessages;
import io.spring.core.user.domain.User;
import io.spring.core.user.domain.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserValidator
    implements ConstraintValidator<UpdateUserConstraint, UpdateUserCommand> {

  private final UserRepository userRepository;

  @Override
  public boolean isValid(UpdateUserCommand value, ConstraintValidatorContext context) {
    if (value == null || value.param() == null || value.targetUser() == null) {
      return false;
    }

    String inputEmail = value.param().email();
    String inputUsername = value.param().username();
    User targetUser = value.targetUser();

    boolean isEmailValid = isEmailAvailable(inputEmail, targetUser);
    boolean isUsernameValid = isUsernameAvailable(inputUsername, targetUser);

    if (isEmailValid && isUsernameValid) {
      return true;
    }

    context.disableDefaultConstraintViolation();
    if (!isEmailValid) {
      addConstraintViolation(context, "email", ErrorMessages.EMAIL_TAKEN);
    }
    if (!isUsernameValid) {
      addConstraintViolation(context, "username", ErrorMessages.USERNAME_TAKEN);
    }

    return false;
  }

  private boolean isEmailAvailable(String email, User targetUser) {
    return email == null
        || email.isEmpty()
        || userRepository.findByEmail(email).map(user -> user.equals(targetUser)).orElse(true);
  }

  private boolean isUsernameAvailable(String username, User targetUser) {
    return username == null
        || username.isEmpty()
        || userRepository
            .findByUsername(username)
            .map(user -> user.equals(targetUser))
            .orElse(true);
  }

  private void addConstraintViolation(
      ConstraintValidatorContext context, String property, String message) {
    context
        .buildConstraintViolationWithTemplate(message)
        .addPropertyNode(property)
        .addConstraintViolation();
  }
}
