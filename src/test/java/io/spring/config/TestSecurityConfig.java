package io.spring.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@TestConfiguration
public class TestSecurityConfig {

  @Bean
  public SecurityContext securityContext() {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(new TestingAuthenticationToken("testUser", "password", "ROLE_USER"));
    SecurityContextHolder.setContext(context);
    return context;
  }
}
