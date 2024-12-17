package io.spring.infrastructure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.OAS_30) // OpenAPI 3.0 기반
        .select()
        .apis(RequestHandlerSelectors.any()) // API 탐색 대상 패키지 설정
        .paths(PathSelectors.any()) // 모든 경로 포함
        .build();
  }
}
