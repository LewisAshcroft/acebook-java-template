package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {


    List<Post> findAll();

    List<Post> findAllByUserId(Long id);

    @Query("SELECT p FROM Post p WHERE p.userId = :userId AND p.isPublic = true")
    Iterable<Post> findByUserIdAndIsPublic(@Param("userId") Long userId);

    @Query("SELECT p FROM Post p WHERE p.userId = :userId AND " +
            "(p.isPublic = true OR p.userId = :currentUserId)")
    Iterable<Post> findByUserIdAndIsPublicOrUserId(@Param("userId") Long userId,
                                                   @Param("currentUserId") Long currentUserId);

    @Query("SELECT p FROM Post p WHERE p.userId = :userId AND p.isPublic = TRUE")
    List<Post> findPublicPostsByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Post p WHERE p.isPublic = true OR p.userId = :userId OR EXISTS (" +
            "SELECT 1 FROM Friend f WHERE f.id.user1Id = :userId AND f.id.user2Id = p.userId AND f.status = 'accepted' " +
            "OR f.id.user1Id = p.userId AND f.id.user2Id = :userId AND f.status = 'accepted')")
    List<Post> findVisiblePosts(@Param("userId") Long userId);
}
