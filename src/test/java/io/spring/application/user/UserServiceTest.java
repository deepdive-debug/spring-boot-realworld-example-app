package io.spring.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.spring.api.exception.InvalidAuthenticationException;
import io.spring.api.exception.ResourceNotFoundException;
import io.spring.api.user.request.LoginParam;
import io.spring.api.user.request.RegisterParam;
import io.spring.api.user.request.UpdateUserCommand;
import io.spring.api.user.request.UpdateUserParam;
import io.spring.api.user.response.UserPersistResponse;
import io.spring.api.user.response.UserWithToken;
import io.spring.core.user.domain.User;
import io.spring.core.user.domain.UserRepository;
import io.spring.core.user.infrastructure.FakeUserRepository;
import io.spring.infrastructure.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {
  @Mock private JwtService jwtService;
  private PasswordEncoder passwordEncoder;
  private UserService userService;
  private User user;

  @BeforeEach
  void setUp() {
    this.passwordEncoder = new BCryptPasswordEncoder();
    UserRepository userRepository = new FakeUserRepository();
    UserValidator userValidator = new UserValidator(userRepository);
    MockitoAnnotations.openMocks(this);
    userService = new UserService(userRepository, passwordEncoder, jwtService, userValidator);

    user = User.of("test@test.com", "testUser", "password", "bio", "image");
    user = userRepository.save(user);
  }

  @Test
  void shouldCreateUserSuccessfully() {
    // Given
    RegisterParam registerParam = new RegisterParam("test@example.com", "username", "password");

    // When
    UserPersistResponse response = userService.createUser(registerParam);

    // Then
    assertThat(response).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenUserLoginFails() {
    // Given
    LoginParam loginParam = new LoginParam("test@test.com", "wrongPassword");

    // When & Then
    assertThatThrownBy(() -> userService.login(loginParam))
        .isInstanceOf(InvalidAuthenticationException.class);
  }

  @Test
  void shouldUpdateUserSuccessfully() {
    // Given
    UpdateUserParam updateUserParam =
        new UpdateUserParam(
            "newEmail@example.com", "newPassword", "newUsername", "newBio", "newImage");
    UpdateUserCommand command = new UpdateUserCommand(user, updateUserParam);

    // When
    userService.updateUser(command);

    // Then
    assertThat(user.getEmail()).isEqualTo("newEmail@example.com");
    assertThat(user.getUsername()).isEqualTo("newUsername");
    assertThat(user.getPassword()).isEqualTo("newPassword");
    assertThat(user.getBio()).isEqualTo("newBio");
    assertThat(user.getImage()).isEqualTo("newImage");
  }

  @Test
  void shouldThrowExceptionWhenUserNotFoundByEmail() {
    // Given
    String email = "notfound@example.com";

    // When & Then
    assertThatThrownBy(() -> userService.findByEmail(email))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void shouldLoginSuccessfully() {
    // Given
    RegisterParam registerParam = new RegisterParam("test@example.com", "username", "password");
    userService.createUser(registerParam);
    LoginParam loginParam = new LoginParam("test@example.com", "password");

    // When
    UserWithToken userWithToken = userService.login(loginParam);

    // Then
    assertThat(userWithToken).isNotNull();
  }
}
