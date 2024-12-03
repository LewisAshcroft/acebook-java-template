package com.makersacademy.acebook.service;

import com.makersacademy.acebook.model.Friend;
import com.makersacademy.acebook.model.FriendId;
import com.makersacademy.acebook.repository.FriendRepository;
import org.springframework.stereotype.Service;

@Service
public class FriendService {
    private final FriendRepository friendRepository;
    private final AuthService authService;

    public FriendService(FriendRepository friendRepository, AuthService authService) {
        this.friendRepository = friendRepository;
        this.authService = authService;
    }

    public void sendFriendRequest(Long recipientId) {
        Long senderId = authService.getCurrentUserId();  // Get current user's ID
        if (senderId == null || senderId.equals(recipientId)) {
            throw new IllegalArgumentException("Invalid sender or recipient.");
        }

        FriendId friendId = new FriendId(senderId, recipientId);

        if (friendRepository.existsById(friendId)) {
            throw new IllegalStateException("Friend request already exists.");
        }

        Friend friend = new Friend(friendId, "pending");  // Create a new pending request
        friendRepository.save(friend);
    }

    public void acceptFriendRequest(Long senderId) {
        Long recipientId = authService.getCurrentUserId();  // Get current user's ID
        FriendId friendId = new FriendId(senderId, recipientId);

        Friend friendship = friendRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        if (!"pending".equals(friendship.getStatus())) {
            throw new IllegalStateException("Friend request is not pending");
        }

        friendship.setStatus("accepted");  // Change status to accepted
        friendRepository.save(friendship);
    }

    public void removeFriend(Long friendId) {
        Long currentUserId = authService.getCurrentUserId();
        if (currentUserId == null) {
            throw new IllegalArgumentException("User not authenticated.");
        }
        // Delete the friendship in either direction.
        friendRepository.deleteById(new FriendId(currentUserId, friendId));
        friendRepository.deleteById(new FriendId(friendId, currentUserId));
    }
}
