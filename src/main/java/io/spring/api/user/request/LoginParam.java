package io.spring.api.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginParam(
    @NotBlank(message = "can't be empty") @Email(message = "should be an email") String email,
    @NotBlank(message = "can't be empty") String password) {}
