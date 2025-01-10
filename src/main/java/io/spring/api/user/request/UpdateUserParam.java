package io.spring.api.user.request;

import jakarta.validation.constraints.Email;

public record UpdateUserParam(
    @Email(message = "should be an email") String email,
    String password,
    String username,
    String bio,
    String image) {}
