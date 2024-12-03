package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.service.AuthService;
import com.makersacademy.acebook.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/friends")
public class FriendController {

    @Autowired
    private final FriendService friendService;

    @Autowired
    private final AuthService authService;

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
}
