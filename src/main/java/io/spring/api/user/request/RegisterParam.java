package io.spring.api.user.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.spring.application.user.DuplicatedEmailConstraint;
import io.spring.application.user.DuplicatedUsernameConstraint;

@JsonRootName("user")
public record RegisterParam(
    @NotBlank(message = "can't be empty")
    @Email(message = "should be an email")
    @DuplicatedEmailConstraint
    String email,

    @NotBlank(message = "can't be empty")
    @DuplicatedUsernameConstraint
    String username,

    @NotBlank(message = "can't be empty")
    String password
) {}
