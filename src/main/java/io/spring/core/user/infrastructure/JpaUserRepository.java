package io.spring.core.user.infrastructure;

import io.spring.core.user.domain.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  // void saveRelation(FollowRelation followRelation);
  //
  // Optional<FollowRelation> findRelation(String userId, String targetId);
  //
  // void removeRelation(FollowRelation followRelation);
}
