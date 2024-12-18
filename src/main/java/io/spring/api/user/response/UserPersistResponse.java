package io.spring.api.user.response;

import io.spring.core.user.User;
import lombok.Builder;

@Builder
public record UserPersistResponse(
	String id
) {
	public static UserPersistResponse from(User user) {
		return UserPersistResponse.builder()
			.id(user.getId())
			.build();
	}
}
