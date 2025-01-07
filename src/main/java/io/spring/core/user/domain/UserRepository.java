package io.spring.core.user.domain;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findById(UUID id);

  User save(User user);

  // void saveRelation(FollowRelation followRelation);
  //
  // Optional<FollowRelation> findRelation(String userId, String targetId);
  //
  // void removeRelation(FollowRelation followRelation);
}
