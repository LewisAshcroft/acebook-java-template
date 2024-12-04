package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Friend;
import com.makersacademy.acebook.service.AuthService;
import com.makersacademy.acebook.service.FriendService;
import com.makersacademy.acebook.repository.FriendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private final FriendService friendService;

    @Autowired
    private final AuthService authService;
    @Autowired
    private FriendRepository friendRepository;

    public FriendController(FriendService friendService, AuthService authService) {
        this.friendService = friendService;
        this.authService = authService;
    }

    // Send a friend request
    @PostMapping("/request/{recipientId}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable Long recipientId) {
        Long senderId = authService.getCurrentUserId();
        if (senderId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        try {
            friendService.sendFriendRequest(recipientId);
            return ResponseEntity.ok("Friend request sent");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Accept a friend request
    @PutMapping("/accept/{senderId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable Long senderId) {
        Long recipientId = authService.getCurrentUserId();
        if (recipientId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        try {
            friendService.acceptFriendRequest(senderId);
            return ResponseEntity.ok("Friend request accepted");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Remove a friend
    @DeleteMapping("/remove/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable Long friendId) {
        Long userId = authService.getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
        try {
            friendService.removeFriend(friendId);
            return ResponseEntity.ok("Friend removed successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Get a list of the current user's friends
    @GetMapping("/friend-list")
    public String getFriendsList(Model model) {
        Long userId = authService.getCurrentUserId(); // Get the logged-in user's ID
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if the user is not logged in
        }
        List<Map<String, String>> friends = friendService.getFriendsByUserId(userId); // Get friends' names
        model.addAttribute("friends", friends); // Add the list of friends to the model
        return "friend-list"; // Return the view name (test-friends-list.html)
    }

    // Block another user
    @PostMapping("/block/{userId}")
    public ResponseEntity<String> blockUser(@PathVariable("userId") Long userIdToBlock) {
        try {
            Long currentUserId = authService.getCurrentUserId();

            // Ensure the user is authenticated and not blocking themselves
            if (currentUserId == null || currentUserId.equals(userIdToBlock)) {
                // Return a 400 Bad Request if the user is trying to block themselves or is not authenticated
                return ResponseEntity.badRequest().body("You cannot block yourself or you're not authenticated.");
            }

            // Check if a relationship exists between the two users (blocked or not)
            Optional<Friend> existingFriend1 = friendRepository.findByUser1IdAndUser2Id(currentUserId, userIdToBlock);
            Optional<Friend> existingFriend2 = friendRepository.findByUser1IdAndUser2Id(userIdToBlock, currentUserId);

            if (existingFriend1.isPresent()) {
                // If a friendship exists where currentUserId is user1, update the status to 'blocked'
                Friend friend = existingFriend1.get();
                friend.setStatus("blocked");
                friendRepository.save(friend);
            } else if (existingFriend2.isPresent()) {
                // If a friendship exists where currentUserId is user2, update the status to 'blocked'
                Friend friend = existingFriend2.get();
                friend.setStatus("blocked");
                friendRepository.save(friend);
            } else {
                // If no existing relationship, create a new "blocked" entry
                Friend newBlock = new Friend(currentUserId, userIdToBlock, "blocked");
                friendRepository.save(newBlock);
            }

            // Return a success response
            return ResponseEntity.ok("User blocked successfully!");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception to help identify the problem
            // Return a 500 Internal Server Error if something goes wrong
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to block user. Please try again later.");
        }
    }


    // Unblock a user who is currently blocked
    @DeleteMapping("/unblock/{userId}")
    @Transactional
    public ResponseEntity<String> unblockUser(@PathVariable("userId") Long userIdToUnblock) {
        Long currentUserId = authService.getCurrentUserId();

        // Ensure the user is authenticated and not trying to unblock themselves
        if (currentUserId == null || currentUserId.equals(userIdToUnblock)) {
            // Return a 400 Bad Request if the user is trying to unblock themselves or is not authenticated
            return ResponseEntity.badRequest().body("You cannot unblock yourself or you're not authenticated.");
        }

        try {
            // Delete both possible relationships (user1 to user2 and user2 to user1)
            friendRepository.deleteByUser1IdAndUser2Id(currentUserId, userIdToUnblock);
            friendRepository.deleteByUser1IdAndUser2Id(userIdToUnblock, currentUserId);
        } catch (Exception e) {
            // Handle any errors that occur during the deletion (e.g., no relationship found)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unblock user. Please try again later.");
        }

        // Return a success message
        return ResponseEntity.ok("User unblocked successfully!");
    }
}