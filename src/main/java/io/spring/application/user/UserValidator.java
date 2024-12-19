package io.spring.application.user;

import io.spring.core.constant.ErrorMessages;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
	private final UserRepository userRepository;

	public void validateEmailAvailability(String email, User targetUser) {
		if (email != null && !email.isEmpty() &&
			userRepository.findByEmail(email).filter(user -> !user.equals(targetUser)).isPresent()) {
			throw new IllegalArgumentException(ErrorMessages.EMAIL_TAKEN);
		}
	}

	public void validateUsernameAvailability(String username, User targetUser) {
		if (username != null && !username.isEmpty() &&
			userRepository.findByUsername(username).filter(user -> !user.equals(targetUser)).isPresent()) {
			throw new IllegalArgumentException(ErrorMessages.USERNAME_TAKEN);
		}
	}

	public void validateRegistration(String email, String username) {
		if (userRepository.findByEmail(email).isPresent()) {
			throw new IllegalArgumentException(ErrorMessages.EMAIL_TAKEN);
		}
		if (userRepository.findByUsername(username).isPresent()) {
			throw new IllegalArgumentException(ErrorMessages.USERNAME_TAKEN);
		}
	}
}