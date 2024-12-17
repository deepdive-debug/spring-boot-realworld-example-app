package io.spring.application;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.data.UserData;
import io.spring.infrastructure.mybatis.readservice.UserReadService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserQueryService {
  private UserReadService userReadService;

  public UserData findById(String id) {
    return Optional.ofNullable(userReadService.findById(id))
        .orElseThrow(ResourceNotFoundException::new);
  }

}
