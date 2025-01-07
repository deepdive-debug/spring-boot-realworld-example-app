package io.spring.core.user.infrastructure;

import io.spring.core.user.domain.User;
import io.spring.core.user.domain.UserRepository;
import io.swagger.v3.oas.annotations.info.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class FakeUserRepository implements UserRepository {
	private final List<User> data = Collections.synchronizedList(new ArrayList<>());

	@Override
	public Optional<User> findByUsername(String username) {
		return data.stream().filter(user -> user.getUsername().equals(username)).findFirst();
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return data.stream().filter(user -> user.getEmail().equals(email)).findFirst();
	}

	@Override
	public Optional<User> findById(UUID id) {
		return data.stream().filter(user -> user.getId().equals(id)).findFirst();
	}

	@Override
	public User save(User user) {
		User newUser = User.builder()
			.email(user.getEmail())
			.username(user.getUsername())
			.password(user.getPassword())
			.bio(user.getBio())
			.image(user.getImage())
			.build();

		newUser.initId();
		data.add(newUser);
		return newUser;
	}

}
