package io.spring.core.user.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class UserTest {

	@Test
	public void userCreate_success() {
		//given
		User user = User.of("test@email.com", "test", "password", "bio", "image");

		//then
		assertNotNull(user);
		assertEquals("test@email.com", user.getEmail());
		assertEquals("test", user.getUsername());
		assertEquals("password", user.getPassword());
		assertEquals("bio", user.getBio());
		assertEquals("image", user.getImage());
		assertNotNull(user.getArticles());
		assertNotNull(user.getComments());
	}

	@Test
	public void userUpdate_success() {
		//given
		User user = User.of("test@email.com", "test", "password", "bio", "image");

		//when
		user.update("new@email.com", "new", "newpassword", "new bio", "new image");

		//then
		assertEquals("new@email.com", user.getEmail());
		assertEquals("new", user.getUsername());
		assertEquals("newpassword", user.getPassword());
		assertEquals("new bio", user.getBio());
		assertEquals("new image", user.getImage());
	}

}
