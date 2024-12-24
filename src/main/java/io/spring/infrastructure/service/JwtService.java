package io.spring.infrastructure.service;

import io.spring.core.user.domain.User;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public sealed interface JwtService permits DefaultJwtService {
  String toToken(User user);

  Optional<String> getSubFromToken(String token);
}
