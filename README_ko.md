
# ![RealWorld Example App using Kotlin and Spring](example-logo.png)

[![Gradle Build & Test](https://github.com/deepdive-debug/spring-boot-realworld-example-app/actions/workflows/gradle.yml/badge.svg)](https://github.com/deepdive-debug/spring-boot-realworld-example-app/actions/workflows/gradle.yml)
[![Test Coverage](https://github.com/deepdive-debug/spring-boot-realworld-example-app/actions/workflows/pull-request-test-coverage.yml/badge.svg)](https://github.com/deepdive-debug/spring-boot-realworld-example-app/actions/workflows/pull-request-test-coverage.yml)
[![Codecov](https://codecov.io/gh/deepdive-debug/spring-boot-realworld-example-app/branch/develop/graph/badge.svg)](https://codecov.io/gh/deepdive-debug/spring-boot-realworld-example-app)

> ### Spring Boot + JPA 기반의 실무 예제 코드베이스 (CRUD, 인증, 고급 패턴 등)
[RealWorld](https://github.com/gothinkster/realworld-example-apps) 사양 및 API를 준수합니다.

이 코드베이스는 다음을 배우고자 하는 개발자들에게 적합합니다:
- Spring Boot를 활용한 최고의 예제 연습.
- JPA와 Spring Security를 활용한 프로덕션급 백엔드 구현.
- CI/CD 워크플로우 및 자동 배포 프로세스 이해.

이 프로젝트는 CRUD 작업, 인증, 라우팅, 페이지네이션 등을 포함하는 Spring Boot + Spring Data JPA로 애플리케이션을 구현하는 것을 시연합니다.

다른 프론트엔드/백엔드와의 동작 방식에 대한 자세한 내용은 [RealWorld](https://github.com/gothinkster/realworld) 저장소를 참조하세요.

---

## 목차

- [작동 원리](#작동-원리)
- [시작하기](#시작하기)
- [로컬 개발 환경 설정](#로컬-개발-환경-설정)
- [보안](#보안)
- [데이터베이스](#데이터베이스)
- [API 문서](#api-문서)
- [Docker로 실행하기](#docker로-실행하기)
- [RealWorld 프론트엔드와 함께 사용하기](#realworld-프론트엔드와-함께-사용하기)
- [테스트 실행](#테스트-실행)
- [코드 포맷팅](#코드-포맷팅)
- [CI/CD 파이프라인 설정](#cicd-파이프라인-설정)
    - [서버 요구 사항](#1-서버-요구-사항)
    - [GitHub Actions Secrets](#2-github-actions-secrets)
    - [배포 워크플로우](#3-배포-워크플로우)
- [도움말](#도움말)
- [기여 가이드라인](#기여-가이드라인)



# 작동 원리

이 애플리케이션은 Spring Boot (Web, Spring Data JPA)를 사용합니다.

* **도메인 주도 설계(DDD)**: 비즈니스 로직과 인프라 로직을 분리합니다.
* **Spring Data JPA**: MyBatis를 대체하여 데이터 액세스 계층을 단순화합니다.

코드 구조는 다음과 같습니다:

1. `api`: Spring MVC와 DTO(Data Transfer Objects)를 사용하는 프레젠테이션 계층.
2. `core`: 비즈니스 로직을 처리하는 서비스 계층.
3. `application`: 도메인 엔티티와 리포지토리를 관리합니다.
4. `infrastructure`: 구성 및 레거시 코드 (Mybatis 관련).


# 시작하기

Java 17이 필요합니다.

```
./gradlew bootRun
```

실행 후 [http://localhost:8080/tags](http://localhost:8080/tags)로 브라우저를 열어 테스트하세요.  
또는 아래 명령어를 실행하세요:

```
curl http://localhost:8080/tags
```


# 로컬 개발 환경 설정

프로젝트를 로컬에서 실행하려면 `.env` 파일을 사용하여 환경 변수를 설정하세요.

### `.env` 파일 예시

    SPRING_PROFILES_ACTIVE=local
    DB_HOST=localhost
    DB_PORT=3306
    DB_NAME=debug
    DB_USERNAME=root
    DB_PASSWORD=password


환경 변수는 `.env` 파일을 루트 디렉토리에 배치하고 `dotenv` 같은 라이브러리를 사용하여 로드할 수 있습니다.



# 보안

Spring Security와 JWT 토큰 처리를 위한 추가 필터가 통합되어 있습니다.  
비밀 키는 `application.properties`에 저장됩니다.



# 데이터베이스

이 애플리케이션은 MySQL을 데이터 영속성에 사용합니다.  
`application.properties` 파일에서 데이터베이스를 구성할 수 있습니다.  
개발 및 로컬 테스트에서는 다른 데이터베이스로 쉽게 전환할 수 있습니다.



# API 문서

Swagger가 통합되어 상세한 API 문서를 제공합니다.  
애플리케이션 실행 후 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)에서 Swagger UI를 확인하세요.



# Docker로 실행하기

Docker가 설치되어 있어야 합니다.


    ./gradlew bootBuildImage --imageName spring-boot-realworld-example-app
    docker run -p 8081:8080 spring-boot-realworld-example-app




# RealWorld 프론트엔드와 함께 사용하기

백엔드 API의 엔드포인트는 [http://localhost:8080](http://localhost:8080)입니다.  
일부 프론트엔드 문서에 명시된 ~~http://localhost:8080/api~~ 는 사용하지 않습니다.



# 테스트 실행

이 저장소는 API와 리포지토리 계층을 포괄하는 다양한 테스트 케이스를 포함하고 있습니다.


    ./gradlew test


#### 테스트 커버리지 보고서 확인
- **HTML 보고서**: `build/reports/tests/test/index.html`에 위치.
- **Codecov 보고서**: [Codecov 대시보드](https://codecov.io/gh/deepdive-debug/spring-boot-realworld-example-app)에서 확인 가능.


#### 커버리지 목표
이 프로젝트는 코드 품질과 신뢰성을 보장하기 위해 최소 70%의 테스트 커버리지를 유지하는 것을 목표로 합니다.


# 코드 포맷팅

Spotless를 사용하여 코드 포맷팅.

    ./gradlew spotlessJavaApply



# CI/CD 파이프라인 설정

이 프로젝트에는 배포 및 테스트 프로세스를 간소화하기 위한 완전 자동화된 CI/CD 파이프라인이 포함되어 있습니다.  
다음 단계를 따라 파이프라인을 설정하세요:

### 1. 서버 요구 사항:
- Docker와 Docker Compose를 서버에 설치하세요.
- 서버 리소스가 제한적인 경우 스왑 메모리를 구성하세요.

### 2. GitHub Actions Secrets:
GitHub 저장소에서 **Settings > Secrets and variables > Actions**에 다음 Secrets를 추가하세요.

**일반**
`CODECOV_TOKEN`

**Docker Hub 액세스**
`DOCKER_USERNAME`, `DOCKER_PASSWORD`, `DOCKER_COMPOSE_YAML_PATH`

**서버 배포**
`SERVER_HOST`, `SERVER_PORT`, `SERVER_SSH_KEY`, `SERVER_USERNAME`


### 3. 배포 워크플로우:
푸시 또는 PR(Pull Request) 시 파이프라인이 자동으로 실행됩니다:
- 애플리케이션을 빌드 및 테스트합니다.
- Docker 이미지를 Docker Hub에 푸시합니다.
- 서버에서 docker-compose를 사용해 이미지를 배포합니다.


# 도움말

이 프로젝트를 포크하고 개선을 위한 PR(Pull Request)을 제출하세요.


## 기여 가이드라인
1. **브랜치 전략**:
    - `feature/<feature-name>` 형식으로 기능 브랜치를 생성하세요.
    - 버그 수정 시 `bugfix/<issue-name>` 형식을 사용하세요.


2. **커밋 메시지 가이드라인**:
    - 다음 형식을 사용하세요:
      ```
      <type>: <subject>
      ```
    - **type**: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`
    - 예시: `feat: add user authentication`


3. **Pull Request**:
    - 변경 사항을 자세히 설명하세요.
    - 관련 이슈를 링크하세요 (예: `Close #1`).
