package io.spring.core.user.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  // void saveRelation(FollowRelation followRelation);
  //
  // Optional<FollowRelation> findRelation(String userId, String targetId);
  //
  // void removeRelation(FollowRelation followRelation);
}
