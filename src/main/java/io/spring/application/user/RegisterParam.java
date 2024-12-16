package io.spring.application.user;

import com.fasterxml.jackson.annotation.JsonRootName;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@JsonRootName("user")
public record RegisterParam(
    @NotBlank(message = "can't be empty")
        @Email(message = "should be an email")
        @DuplicatedEmailConstraint
        String email,
    @NotBlank(message = "can't be empty") @DuplicatedUsernameConstraint String username,
    @NotBlank(message = "can't be empty") String password) {}
