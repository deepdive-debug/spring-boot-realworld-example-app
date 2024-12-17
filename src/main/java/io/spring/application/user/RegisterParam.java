
package io.spring.application.user;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonRootName("user")
public record RegisterParam(
    @NotBlank(message = "can't be empty")
        @Email(message = "should be an email")
        @DuplicatedEmailConstraint
        String email,
    @NotBlank(message = "can't be empty") @DuplicatedUsernameConstraint String username,
    @NotBlank(message = "can't be empty") String password) {}