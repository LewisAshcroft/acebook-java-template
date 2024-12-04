package com.makersacademy.acebook.model;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PostTest {

	private Post post = new Post("hello", "", 1L, false, null, null);

	@Test
	public void postHasContent() {
		assertThat(post.getContent(), containsString("hello"));
	}

	@Test
	public void testPostVisibility() {
		Post post = new Post("Test Content", "", 1L, true, null, null);
		assertTrue(post.getIsPublic(), "Post should be public by default");

		post.setIsPublic(false);
		assertFalse(post.getIsPublic(), "Post should be private after update");
	}

	@Test
	public void postHasTimestamps() {
		Post post = new Post("Post with timestamp", "", 1L, true, null, null);
		LocalDateTime now = LocalDateTime.now();
		Timestamp createdAt = post.getCreatedAt();
		Timestamp updatedAt = post.getUpdatedAt();

		assertTrue(createdAt.toLocalDateTime().isBefore(now.plusSeconds(1)), "CreatedAt should be close to now");
		assertTrue(updatedAt.toLocalDateTime().isBefore(now.plusSeconds(1)), "UpdatedAt should be close to now");
	}
}