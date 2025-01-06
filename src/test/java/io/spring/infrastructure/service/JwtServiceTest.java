package io.spring.infrastructure.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.spring.core.user.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class JwtServiceTest {

  @Mock private DefaultJwtService defaultJwtService;

  private JwtService jwtService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    jwtService = defaultJwtService;
  }

  @Test
  void shouldGenerateTokenForUser() {
    // Given
    User user = mock(User.class);
    when(user.getId()).thenReturn("user-id");
    when(defaultJwtService.toToken(user)).thenReturn("mocked-token");

    // When
    String token = jwtService.toToken(user);

    // Then
    assertThat(token).isEqualTo("mocked-token");
    verify(defaultJwtService).toToken(user);
  }

  @Test
  void shouldExtractSubjectFromToken() {
    // Given
    String token = "mocked-token";
    when(defaultJwtService.getSubFromToken(token)).thenReturn(Optional.of("user-id"));

    // When
    Optional<String> userId = jwtService.getSubFromToken(token);

    // Then
    assertThat(userId).isPresent();
    assertThat(userId.get()).isEqualTo("user-id");
    verify(defaultJwtService).getSubFromToken(token);
  }

  @Test
  void shouldReturnEmptyWhenTokenIsInvalid() {
    // Given
    String invalidToken = "invalid-token";
    when(defaultJwtService.getSubFromToken(invalidToken)).thenReturn(Optional.empty());

    // When
    Optional<String> userId = jwtService.getSubFromToken(invalidToken);

    // Then
    assertThat(userId).isEmpty();
    verify(defaultJwtService).getSubFromToken(invalidToken);
  }
}
