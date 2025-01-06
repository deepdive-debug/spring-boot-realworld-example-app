package io.spring.core.user.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class FollowRelationTest {
	@Test
	public void followCreate_success() {
		//given
		FollowRelation relation = FollowRelation.of("follower", "followee");

		//then
		assertNotNull(relation);
		assertEquals("follower", relation.getUserId());
		assertEquals("followee", relation.getTargetId());
	}
}
