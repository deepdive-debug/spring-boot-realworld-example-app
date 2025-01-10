# ![RealWorld Example App using Kotlin and Spring](example-logo.png)

[![Gradle Build & Test](https://github.com/deepdive-debug/spring-boot-realworld-example-app/actions/workflows/gradle.yml/badge.svg)](https://github.com/deepdive-debug/spring-boot-realworld-example-app/actions/workflows/gradle.yml)
[![Test Coverage](https://github.com/deepdive-debug/spring-boot-realworld-example-app/actions/workflows/pull-request-test-coverage.yml/badge.svg)](https://github.com/deepdive-debug/spring-boot-realworld-example-app/actions/workflows/pull-request-test-coverage.yml)
[![Codecov](https://codecov.io/gh/deepdive-debug/spring-boot-realworld-example-app/branch/develop/graph/badge.svg)](https://codecov.io/gh/deepdive-debug/spring-boot-realworld-example-app)


> ### Spring boot + JPA codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld-example-apps) spec and API.

This codebase is ideal for developers looking to:
- Learn Spring Boot with a focus on best practices.
- Implement a production-grade backend using JPA and Spring Security.
- Understand CI/CD workflows and automated deployment processes.


This codebase was created to demonstrate a fully fledged full-stack application built with Spring boot + Spring Data JPA including CRUD operations, authentication, routing, pagination, and more.

For more information on how to this works with other frontends/backends, head over to the [RealWorld](https://github.com/gothinkster/realworld) repo.

---

## Table of Contents

- [How It Works](#how-it-works)
- [Getting Started](#getting-started)
- [Local Development Setup](#local-development-setup)
- [Security](#security)
- [Database](#database)
- [API Documentation](#api-documentation)
- [Try It Out with Docker](#try-it-out-with-docker)
- [Try It Out with a RealWorld Frontend](#try-it-out-with-a-realworld-frontend)
- [Run Tests](#run-tests)
- [Code Format](#code-format)
- [CI/CD Pipeline Setup](#cicd-pipeline-setup)
    - [Server Requirements](#1-server-requirements)
    - [GitHub Actions Secrets](#2-github-actions-secrets)
    - [Deployment Workflow](#3-deployment-workflow)
- [Help](#help)


# How it works

The application uses Spring Boot (Web, Spring Data JPA).

* **Domain Driven Design (DDD)**: Separates business logic from infrastructure logic.
* **Spring Data JPA**: Simplifies data access layers, replacing MyBatis.

And the code is organized as this:

1. `api` Handles presentation layer with Spring MVC and Data Transfer Objects (DTOs).
2. `core` Contains service layer for business logic.
3. `application` Manages domain entities and repositories.
4. `infrastructure` Handles configurations and legacy integrations.


# Getting started

You'll need Java 17 installed.

    ./gradlew bootRun

To test that it works, open a browser tab at http://localhost:8080/tags .  
Alternatively, you can run

    curl http://localhost:8080/tags


# Local Development Setup

To run the project locally, you can configure your environment variables using a `.env` file.

### Example `.env` file

    SPRING_PROFILES_ACTIVE=local
    DB_HOST=localhost
    DB_PORT=3306
    DB_NAME=debug
    DB_USERNAME=root
    DB_PASSWORD=password


Load the environment variables by placing the `.env` file in the root directory and using a library like `dotenv` in your project.


# Security

Integration with Spring Security and add other filter for jwt token process.

The secret key is stored in `application.properties`.


# Database

The application uses MySQL for data persistence. You can configure the database in the `application.properties` file.
For development and local testing, you can easily switch to another database if needed.


# API Documentation

Swagger is integrated to provide detailed API documentation.
After running the application, access Swagger UI at http://localhost:8080/swagger-ui/index.html.


# Try it out with [Docker](https://www.docker.com/)

You'll need Docker installed.

    ./gradlew bootBuildImage --imageName spring-boot-realworld-example-app
    docker run -p 8081:8080 spring-boot-realworld-example-app

# Try it out with a RealWorld frontend

The entry point address of the backend API is at http://localhost:8080, **not** ~~http://localhost:8080/api~~ as some of the frontend documentation suggests.


# Run test

The repository contains a wide range of test cases to cover both API and repository layers.

    ./gradlew test

#### Access Test Coverage Reports
After running the tests, you can access the coverage report.
- **HTML Report:** Located at `build/reports/tests/test/index.html`.
- **Codecov Report:** Available at [Codecov Dashboard](https://codecov.io/gh/deepdive-debug/spring-boot-realworld-example-app).

#### Coverage Goals
This project aims to maintain a minimum of 70% test coverage to ensure code quality and reliability.


# Code format

Use spotless for code format.

    ./gradlew spotlessJavaApply


# CI/CD

This project includes a fully automated CI/CD pipeline to streamline deployment and testing processes. Follow these steps to set up the pipeline.

### 1.	Server Requirements
- Install Docker and Docker Compose on your server.
- Configure swap memory if your server has limited resources.

### 2. GitHub Actions Secrets
Add the following secrets to your GitHub repository under **Settings > Secrets and variables > Actions**.

**General**
`CODECOV_TOKEN`

**Docker Hub Access**
`DOCKER_USERNAME`, `DOCKER_PASSWORD`, `DOCKER_COMPOSE_YAML_PATH`

**Server Deployment**
`SERVER_HOST`, `SERVER_PORT`, `SERVER_SSH_KEY`, `SERVER_USERNAME`


### 3. Deployment Workflow
On every push or pull request, the pipeline will:
- Build and test the application.
- Push the Docker image to Docker Hub.
- Deploy the image to your server using docker-compose.


# Help

Please fork and PR to improve the project.

## Contribution Guidelines
### Branch Strategy
- Create feature branches using the naming convention `feature/<feature-name>`.
- Use `refactor/<issue-name>` for refactoring codes.


### Commit Message Guidelines

Use the format

    <type>: <subject>

**Types**: `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

Example: `feat: add user authentication`



### Pull Request
- Describe the changes in detail.
- Link related issues (e.g., `Closes #1`).
