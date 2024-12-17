package io.spring.api.user.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public record LoginParam(
	@NotBlank(message = "can't be empty")
	@Email(message = "should be an email")
	String email,

	@NotBlank(message = "can't be empty")
	String password
) {}