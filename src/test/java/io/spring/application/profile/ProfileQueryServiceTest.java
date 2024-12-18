package io.spring.application.profile;

import io.spring.api.user.response.ProfileData;
import io.spring.application.ProfileQueryService;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import io.spring.infrastructure.DbTestBase;
import io.spring.infrastructure.repository.MyBatisUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@Import({ProfileQueryService.class, MyBatisUserRepository.class})
public class ProfileQueryServiceTest extends DbTestBase {
  @Autowired private ProfileQueryService profileQueryService;
  @Autowired private UserRepository userRepository;

  @Test
  public void should_fetch_profile_success() {
    User currentUser = User.of("a@test.com", "a", "123", "", "");
    User profileUser = User.of("p@test.com", "p", "123", "", "");
    userRepository.save(profileUser);

    ProfileData profileData =
        profileQueryService.findByUsername(profileUser.getUsername(), currentUser);
	  Assertions.assertNotNull(profileData);
  }
}
