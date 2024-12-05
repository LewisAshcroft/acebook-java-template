package com.makersacademy.acebook.repository;


import com.makersacademy.acebook.model.Friend;
import com.makersacademy.acebook.model.FriendId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends CrudRepository<Friend, FriendId> {

    // Query by user1Id
    List<Friend> findById_User1Id(Long user1Id);

    // Query by user2Id
    List<Friend> findById_User2Id(Long user2Id);

    Optional<Friend> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
    Optional<Friend> findByUser2IdAndUser1Id(Long user2Id, Long user1Id);

    void deleteByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    // Query to fetch the relationship status between two users, if it exists
    @Query("SELECT f.status FROM Friend f WHERE " +
            "(f.id.user1Id = :currentUserId AND f.id.user2Id = :otherUserId) OR " +
            "(f.id.user1Id = :otherUserId AND f.id.user2Id = :currentUserId)")
    Optional<String> findRelationshipStatus(@Param("currentUserId") Long currentUserId, @Param("otherUserId") Long otherUserId);

    // Query to fetch friends' first and last names where the status is accepted
    @Query("SELECT u.firstName, u.lastName FROM User u WHERE u.id IN (" +
            "SELECT f.id.user1Id FROM Friend f WHERE f.id.user2Id = :userId AND f.status = 'accepted' " +
            "UNION " +
            "SELECT f.id.user2Id FROM Friend f WHERE f.id.user1Id = :userId AND f.status = 'accepted')")
    List<Object[]> findFriendNamesByUserId(@Param("userId") Long userId);

    boolean existsByUser1IdAndUser2IdAndStatus(Long currentUserId, Long otherUserId, String status);
}
