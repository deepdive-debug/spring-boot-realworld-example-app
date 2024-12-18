package io.spring.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.spring.api.security.WebSecurityConfig;
import io.spring.api.user.UsersApi;
import io.spring.api.user.response.UserData;
import io.spring.api.user.response.UserPersistResponse;
import io.spring.application.user.UserService;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import io.spring.infrastructure.service.DefaultJwtService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UsersApi.class)
@Import({WebSecurityConfig.class})
public class UsersApiTest {

  @Autowired private MockMvc mvc;

  @MockBean private UserRepository userRepository;
  @MockBean private DefaultJwtService jwtService;
  @MockBean private UserService userService;

  @Autowired private PasswordEncoder passwordEncoder;

  private String defaultAvatar;

  @BeforeEach
  public void setUp() {
    // given: 초기화 및 설정
    RestAssuredMockMvc.mockMvc(mvc);
    defaultAvatar = "https://static.productionready.io/images/smiley-cyrus.jpg";
  }

  @Test
  public void should_create_user_successfully() {
    // given: 준비
    String email = "john@jacob.com";
    String username = "johnjacob";
    User user = User.of(email, username, "123", "", defaultAvatar);
    Map<String, Object> param = prepareRegisterParameter(email, username);

    when(userService.createUser(any())).thenReturn(UserPersistResponse.from(user));
    when(jwtService.toToken(any())).thenReturn("123");

    // when: 요청
    given()
        .contentType("application/json")
        .body(param)
        .when()
        .post("/users")

        // then: 검증
        .then()
        .statusCode(201)
        .body("user.email", equalTo(email))
        .body("user.username", equalTo(username))
        .body("user.image", equalTo(defaultAvatar))
        .body("user.token", equalTo("123"));

    verify(userService).createUser(any());
  }

  @Test
  public void should_return_error_for_invalid_email() {
    // given: 준비
    Map<String, Object> param = prepareRegisterParameter("invalidEmail", "johnjacob");

    // when: 요청
    given()
        .contentType("application/json")
        .body(param)
        .when()
        .post("/users")

        // then: 검증
        .then()
        .statusCode(422)
        .body("errors.email[0]", equalTo("should be an email"));
  }

  @Test
  public void should_return_error_for_duplicated_username() {
    // given: 준비
    String email = "john@jacob.com";
    String username = "johnjacob";
    Map<String, Object> param = prepareRegisterParameter(email, username);

    when(userRepository.findByUsername(eq(username)))
        .thenReturn(Optional.of(User.of(email, username, "123", "bio", "")));

    // when: 요청
    given()
        .contentType("application/json")
        .body(param)
        .when()
        .post("/users")

        // then: 검증
        .then()
        .statusCode(422)
        .body("errors.username[0]", equalTo("duplicated username"));
  }

  @Test
  public void should_login_successfully() {
    // given: 준비
    String email = "john@jacob.com";
    String username = "johnjacob";
    String password = "123";
    User user = User.of(email, username, passwordEncoder.encode(password), "", defaultAvatar);
    Map<String, Object> param = prepareLoginParameter(email, password);

    when(jwtService.toToken(any())).thenReturn("token123");

    // when: 요청
    given()
        .contentType("application/json")
        .body(param)
        .when()
        .post("/users/login")

        // then: 검증
        .then()
        .statusCode(200)
        .body("user.email", equalTo(email))
        .body("user.username", equalTo(username))
        .body("user.token", equalTo("token123"));
  }

  @Test
  public void should_fail_login_with_wrong_password() {
    // given: 준비
    String email = "john@jacob.com";
    Map<String, Object> param = prepareLoginParameter(email, "wrongPassword");

    when(userService.login(any()))
        .thenThrow(new IllegalArgumentException("invalid email or password"));

    // when: 요청
    given()
        .contentType("application/json")
        .body(param)
        .when()
        .post("/users/login")

        // then: 검증
        .then()
        .statusCode(422)
        .body("message", equalTo("invalid email or password"));
  }

  private Map<String, Object> prepareRegisterParameter(String email, String username) {
    return Map.of(
        "user",
        Map.of(
            "email", email,
            "password", "12345678",
            "username", username));
  }

  private Map<String, Object> prepareLoginParameter(String email, String password) {
    return Map.of(
        "user",
        Map.of(
            "email", email,
            "password", password));
  }
}
