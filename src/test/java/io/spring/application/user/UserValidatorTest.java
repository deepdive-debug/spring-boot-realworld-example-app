package io.spring.application.user;

import io.spring.core.user.domain.User;
import io.spring.core.user.domain.UserRepository;
import io.spring.core.user.infrastructure.FakeUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserValidatorTest {
  private UserValidator userValidator;
  private User user;
  private User anotherUser;

  @BeforeEach
  void setUp() {
    UserRepository userRepository = new FakeUserRepository();
    userValidator = new UserValidator(userRepository);

    user = User.of("test@test.com", "testUser", "password", "bio", "image");
    user = userRepository.save(user);

    anotherUser = User.of("none@test.com", "anotherUser", "password", "bio", "image");
    anotherUser = userRepository.save(anotherUser);
  }

  @Test
  void validateEmailAvailability_success() {
    userValidator.validateEmailAvailability("test@test.com", user);
  }

  @Test
  void validateEmailAvailability_throws_IllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> userValidator.validateEmailAvailability("none@test.com", user));
  }

  @Test
  void validateUsernameAvailability_success() {
    userValidator.validateUsernameAvailability("testUser", user);
  }

  @Test
  void validateUsernameAvailability_throws_IllegalArgumentException() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> userValidator.validateUsernameAvailability("anotherUser", user));
  }

  @Test
  void validateRegistration_success() {
    userValidator.validateRegistration("new@test.com", "newUser");
  }

  @Test
  void validateRegistration_throws_IllegalArgumentException1() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> userValidator.validateRegistration("new@test.com", "testUser"));
  }

  @Test
  void validateRegistration_throws_IllegalArgumentException2() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> userValidator.validateRegistration("test@test.com", "newUser"));
  }
}
