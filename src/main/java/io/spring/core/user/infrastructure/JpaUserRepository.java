package io.spring.core.user.infrastructure;

import io.spring.core.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<User, String> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  // void saveRelation(FollowRelation followRelation);
  //
  // Optional<FollowRelation> findRelation(String userId, String targetId);
  //
  // void removeRelation(FollowRelation followRelation);
}
