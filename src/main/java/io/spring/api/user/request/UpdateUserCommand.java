package io.spring.api.user.request;

import io.spring.application.user.UpdateUserConstraint;
import io.spring.core.user.domain.User;

@UpdateUserConstraint
public record UpdateUserCommand(User targetUser, UpdateUserParam param) {}
