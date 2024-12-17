package io.spring.api.user.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.Email;

@JsonRootName("user")
public record UpdateUserParam(
    @Email(message = "should be an email") String email,
    String password,
    String username,
    String bio,
    String image) {}
