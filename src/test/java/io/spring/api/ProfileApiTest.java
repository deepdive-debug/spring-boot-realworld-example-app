package io.spring.api;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.spring.JacksonCustomizations;
import io.spring.api.security.WebSecurityConfig;
import io.spring.api.user.ProfileApi;
import io.spring.api.user.response.ProfileData;
import io.spring.application.ProfileQueryService;
import io.spring.application.user.UserService;
import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProfileApi.class)
@Import({WebSecurityConfig.class, JacksonCustomizations.class})
public class ProfileApiTest extends TestWithCurrentUser {
  private User anotherUser;

  @Autowired private MockMvc mvc;

  @MockBean private UserService userService;

  @MockBean private ProfileQueryService profileQueryService;

  private ProfileData profileData;

  @BeforeEach
  public void setUp() throws Exception {
    super.setUp();
    RestAssuredMockMvc.mockMvc(mvc);
    anotherUser = User.of("username@test.com", "username", "123", "", "");
    profileData =
        new ProfileData(
            anotherUser.getId(),
            anotherUser.getUsername(),
            anotherUser.getBio(),
            anotherUser.getImage(),
            false);
    when(userService.findByUsername(eq(anotherUser.getUsername()))).thenReturn(anotherUser);
  }

  @Test
  public void should_get_user_profile_success() throws Exception {
    when(profileQueryService.findByUsername(eq(profileData.getUsername()), eq(null)))
        .thenReturn(profileData);
    RestAssuredMockMvc.when()
        .get("/profiles/{username}", profileData.getUsername())
        .prettyPeek()
        .then()
        .statusCode(200)
        .body("profile.username", equalTo(profileData.getUsername()));
  }

  @Test
  public void should_follow_user_success() throws Exception {
    when(profileQueryService.findByUsername(eq(profileData.getUsername()), eq(user)))
        .thenReturn(profileData);
    given()
        .header("Authorization", "Token " + token)
        .when()
        .post("/profiles/{username}/follow", anotherUser.getUsername())
        .prettyPeek()
        .then()
        .statusCode(200);
    verify(userService).saveRelation(FollowRelation.of(user.getId(), anotherUser.getId()));
  }

  @Test
  public void should_unfollow_user_success() throws Exception {
    FollowRelation followRelation = FollowRelation.of(user.getId(), anotherUser.getId());
    when(userService.findRelation(eq(user.getId()), eq(anotherUser.getId())))
        .thenReturn(followRelation);
    when(profileQueryService.findByUsername(eq(profileData.getUsername()), eq(user)))
        .thenReturn(profileData);

    given()
        .header("Authorization", "Token " + token)
        .when()
        .delete("/profiles/{username}/follow", anotherUser.getUsername())
        .prettyPeek()
        .then()
        .statusCode(200);

    verify(userService).removeRelation(eq(followRelation));
  }
}
