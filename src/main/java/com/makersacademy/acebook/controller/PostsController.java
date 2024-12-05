package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Like;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.LikeRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import com.makersacademy.acebook.service.AuthService;
import com.makersacademy.acebook.service.BlockedService;
import com.makersacademy.acebook.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class PostsController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FilesStorageService storageService;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    AuthService authService;
    @Autowired
    BlockedService blockedService;

    @GetMapping("/posts")
    public String index(Model model) {
        // Get the current user's ID
        Long userId = authService.getCurrentUserId();

        if (userId == null) {
            // Redirect to login if the user is not authenticated
            return "redirect:/login";
        }

        // Fetch the authenticated user
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", currentUser);

        // Fetch posts that are either public or belong to the current user's friends
        List<Post> posts = postRepository.findVisiblePosts(userId);

        // Create a list of posts with additional data
        List<Map<String, Object>> postsWithLikeStatus = new ArrayList<>();
        for (Post post : posts) {
            // Ensure that the createdAt field is not null and set a default if it is
            if (post.getCreatedAt() == null) {
                post.setCreatedAt(Timestamp.valueOf(LocalDateTime.now())); // Set a default timestamp if null
            }
            // Skip posts from blocked users
            if (blockedService.isBlocked(userId, post.getUserId()) || blockedService.isBlocked(post.getUserId(), userId)) {
                continue;
            }

            Map<String, Object> postData = new HashMap<>();
            postData.put("post", post);

            // Determine if the current user has liked the post
            boolean isLiked = likeRepository.findByUserIdAndPostId(userId, post.getId()) != null;
            postData.put("isLiked", isLiked);

            // Add the like count
            long likeCount = likeRepository.countByPostId(post.getId());
            postData.put("likeCount", likeCount);

            postsWithLikeStatus.add(postData);
        }

        // Add the structured posts data to the model
        model.addAttribute("posts", postsWithLikeStatus);

        return "posts/index";
    }


    @GetMapping("/new-post")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        return "posts/new-post";
    }

    @PostMapping("/new-post")
    public RedirectView create(
            @RequestParam("file") MultipartFile file,
            @RequestParam("content") String content,
            @RequestParam(value = "isPrivate", required = false) boolean isPrivate) { // Checkbox value

        Long userId = authService.getCurrentUserId();  // Get the currently logged-in user's ID

        if (userId != null) {
            Optional<User> currentUser = userRepository.findById(userId);  // Find the user by user_id

            if (currentUser.isPresent()) {
                User activeUser = currentUser.get();
                Post post = new Post("", "", activeUser.getId(), true, null, null);

                // Set timestamps
                LocalDateTime now = LocalDateTime.now();
                post.setCreatedAt(Timestamp.valueOf(now));
                post.setUpdatedAt(Timestamp.valueOf(now));

                // Set content
                String uploadAddress = storageService.save(file);
                post.setPicture("/files/" + uploadAddress);
                post.setContent(content);

                // Set visibility based on the form checkbox
                post.setIsPublic(!isPrivate);  // Checkbox: if private is checked, isPublic = false

                // Save post to the database
                postRepository.save(post);
            }
        }
        return new RedirectView("/posts");
    }


    // POST request to like a post
    @PostMapping("/like/{postId}")
    @ResponseBody
    public Map<String, Object> likePost(@PathVariable("postId") Long postId) {
        // Get the authenticated user
        Long userId = authService.getCurrentUserId();
        // Get the currently logged-in user's ID
        Optional<User> currentUser = userRepository.findById(userId);

        if (currentUser.isPresent()) {
            User user = currentUser.get();
            Like existingLike = likeRepository.findByUserIdAndPostId(user.getId(), postId);
            if (existingLike == null) {
                Like like = new Like();
                like.setPost(postRepository.findById(postId).orElseThrow());
                like.setUser(user);
                likeRepository.save(like);
            }
        }

        long likeCount = likeRepository.countByPostId(postId);
        return Map.of("likeCount", likeCount);
    }


    @DeleteMapping("/unlike/{postId}")
    @ResponseBody
    public Map<String, Object> unlikePost(@PathVariable("postId") Long postId) {
        // Get the authenticated user
        Long userId = authService.getCurrentUserId();  // Get the currently logged-in user's ID
        Optional<User> currentUser = userRepository.findById(userId);

        if (currentUser.isPresent()) {
            User user = currentUser.get();
            Like existingLike = likeRepository.findByUserIdAndPostId(user.getId(), postId);
            if (existingLike != null) {
                likeRepository.delete(existingLike);
            }
        }

        long likeCount = likeRepository.countByPostId(postId);
        return Map.of("likeCount", likeCount);
    }

    @PostMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable("id") long postId) {
        System.out.println("Attempting to delete post with ID: " + postId);

        Long userId = authService.getCurrentUserId();  // Get the currently logged-in user's ID

        if (userId == null) {
            return "redirect:/posts"; // Do nothing and redirect back to posts
        }

        // Fetch the post to verify ownership
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if (post.getUserId().equals(userId)) {
                postRepository.deleteById(postId); // Delete only if the user owns the post
            }
        }

        return "redirect:/posts";
    }

}
