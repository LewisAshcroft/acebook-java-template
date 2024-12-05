package com.makersacademy.acebook.service;

import com.makersacademy.acebook.model.Friend;
import com.makersacademy.acebook.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlockedService {

    @Autowired
    private FriendRepository friendRepository;

    // Check if a relationship is blocked
    public boolean isBlocked(Long currentUserId, Long otherUserId) {
        Optional<String> statusOpt = friendRepository.findRelationshipStatus(currentUserId, otherUserId);
        return statusOpt.isPresent() && statusOpt.get().equals("blocked");
    }

    // Check if the current user initiated the block
    public boolean isBlockInitiator(Long currentUserId, Long otherUserId) {
        Optional<String> statusOpt = friendRepository.findRelationshipStatus(currentUserId, otherUserId);
        return statusOpt.isPresent() && statusOpt.get().equals("blocked")
                && friendRepository.existsByUser1IdAndUser2IdAndStatus(currentUserId, otherUserId, "blocked");
    }

    // Block a user
    public void blockUser(Long currentUserId, Long userIdToBlock) {
        Friend block = new Friend(currentUserId, userIdToBlock, "blocked");
        friendRepository.save(block);

        // Optionally, block the reverse relationship as well (i.e., user2 blocks user1)
        Friend reverseBlock = new Friend(userIdToBlock, currentUserId, "blocked");
        friendRepository.save(reverseBlock);
    }

    // Unblock a user
    public void unblockUser(Long currentUserId, Long userIdToUnblock) {
        friendRepository.deleteByUser1IdAndUser2Id(currentUserId, userIdToUnblock);
        friendRepository.deleteByUser1IdAndUser2Id(userIdToUnblock, currentUserId);
    }
}

