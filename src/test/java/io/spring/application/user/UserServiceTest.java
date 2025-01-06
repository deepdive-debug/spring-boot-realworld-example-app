package io.spring.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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
import io.spring.infrastructure.service.JwtService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private JwtService jwtService;

  @Mock private UserValidator userValidator;

  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    userService = new UserService(userRepository, passwordEncoder, jwtService, userValidator);
  }

  @Test
  void shouldCreateUserSuccessfully() {
    // Given
    RegisterParam registerParam = new RegisterParam("test@example.com", "username", "password");
    User user = User.of("test@example.com", "username", "encodedPassword", "", "defaultImage");

    doNothing()
        .when(userValidator)
        .validateRegistration(registerParam.email(), registerParam.username());
    when(passwordEncoder.encode(registerParam.password())).thenReturn("encodedPassword");
    when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

    // When
    UserPersistResponse response = userService.createUser(registerParam);

    // Then
    assertThat(response).isNotNull();
    verify(userValidator).validateRegistration(registerParam.email(), registerParam.username());
    verify(userRepository).save(Mockito.any(User.class));
  }

  @Test
  void shouldThrowExceptionWhenUserLoginFails() {
    // Given
    LoginParam loginParam = new LoginParam("test@example.com", "wrongPassword");
    User user = mock(User.class);

    when(userRepository.findByEmail(loginParam.email())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(loginParam.password(), user.getPassword())).thenReturn(false);

    // When & Then
    assertThatThrownBy(() -> userService.login(loginParam))
        .isInstanceOf(InvalidAuthenticationException.class);
    verify(passwordEncoder).matches(loginParam.password(), user.getPassword());
  }

  @Test
  void shouldUpdateUserSuccessfully() {
    // Given
    User user = mock(User.class);
    UpdateUserParam updateUserParam =
        new UpdateUserParam(
            "newEmail@example.com", "newUsername", "newPassword", "newBio", "newImage");
    UpdateUserCommand command = new UpdateUserCommand(user, updateUserParam);

    doNothing().when(userValidator).validateEmailAvailability(updateUserParam.email(), user);
    doNothing().when(userValidator).validateUsernameAvailability(updateUserParam.username(), user);

    // When
    userService.updateUser(command);

    // Then
    verify(userValidator).validateEmailAvailability(updateUserParam.email(), user);
    verify(userValidator).validateUsernameAvailability(updateUserParam.username(), user);
    verify(user)
        .update(
            updateUserParam.email(),
            updateUserParam.username(),
            updateUserParam.password(),
            updateUserParam.bio(),
            updateUserParam.image());
    verify(userRepository).save(user);
  }

  @Test
  void shouldThrowExceptionWhenUserNotFoundByEmail() {
    // Given
    String email = "notfound@example.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> userService.findByEmail(email))
        .isInstanceOf(ResourceNotFoundException.class);
  }

  @Test
  void shouldLoginSuccessfully() {
    // Given
    LoginParam loginParam = new LoginParam("test@example.com", "password");
    User user = mock(User.class);

    when(userRepository.findByEmail(loginParam.email())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(loginParam.password(), user.getPassword())).thenReturn(true);
    when(jwtService.toToken(user)).thenReturn("jwtToken");

    // When
    UserWithToken userWithToken = userService.login(loginParam);

    // Then
    assertThat(userWithToken.token()).isEqualTo("jwtToken");
    verify(userRepository).findByEmail(loginParam.email());
    verify(passwordEncoder).matches(loginParam.password(), user.getPassword());
    verify(jwtService).toToken(user);
  }
}
