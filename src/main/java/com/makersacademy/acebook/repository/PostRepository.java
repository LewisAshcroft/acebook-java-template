package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends CrudRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.user_id = :userId")
    Iterable<Post> findAllByUserId(@Param("userId") Long userId);
}