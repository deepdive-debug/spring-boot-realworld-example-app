package io.spring.application.user;

import io.spring.core.user.User;

@UpdateUserConstraint
public record UpdateUserCommand(
    User targetUser,
    UpdateUserParam param
) {}

