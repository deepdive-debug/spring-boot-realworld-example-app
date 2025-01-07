package io.spring.api.user.request;

import io.spring.api.user.validation.DuplicatedEmailConstraint;
import io.spring.api.user.validation.DuplicatedUsernameConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterParam(
    @NotBlank(message = "can't be empty")
        @Email(message = "should be an email")
        @DuplicatedEmailConstraint
        String email,
    @NotBlank(message = "can't be empty") @DuplicatedUsernameConstraint String username,
    @NotBlank(message = "can't be empty") String password) {}
