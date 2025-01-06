package io.spring.core.user.infrastructure;

import io.spring.core.user.domain.User;
import io.spring.core.user.domain.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final JpaUserRepository jpaUserRepository;

  @Override
  public Optional<User> findByUsername(String username) {
    return jpaUserRepository.findByUsername(username);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return jpaUserRepository.findByEmail(email);
  }

  @Override
  public Optional<User> findById(String id) {
    return jpaUserRepository.findById(id);
  }

  @Override
  public User save(User user) {
    return jpaUserRepository.save(user);
  }
}
