package com.makersacademy.acebook.repository;


import com.makersacademy.acebook.model.Friend;
import com.makersacademy.acebook.model.FriendId;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends CrudRepository<Friend, FriendId> {

    // Query by user1Id
    List<Friend> findById_User1Id(Long user1Id);

    // Query by user2Id
    List<Friend> findById_User2Id(Long user2Id);
}
