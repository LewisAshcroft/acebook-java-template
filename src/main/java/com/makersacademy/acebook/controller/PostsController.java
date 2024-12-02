package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Like;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.LikeRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import com.makersacademy.acebook.service.AuthService;
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
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/posts")
    public String index(Model model) {
        List<Post> posts = postRepository.findAll();
        List<User> users = userRepository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        model.addAttribute("users", users);
        model.addAttribute("user", new User());

        return "posts/index";
    }

    @GetMapping("/new-post")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        return "posts/new-post";
    }

    @PostMapping("/new-post")
    public RedirectView create(@RequestParam("file") MultipartFile file, @RequestParam("content") String content) {
        Long userId = authService.getCurrentUserId();  // Get the currently logged-in user's ID

        if (userId != null) {
            Optional<User> currentUser = userRepository.findById(userId);  // Find the user by user_id

            if (currentUser.isPresent()) {
                User activeUser = currentUser.get();
                Post post = new Post("", "", activeUser.getId(), false, null, null);

                // Set timestamps
                LocalDateTime now = LocalDateTime.now();
                post.setCreatedAt(Timestamp.valueOf(now));
                post.setUpdatedAt(Timestamp.valueOf(now));

                // Set content of the post
                String uploadAddress = storageService.save(file);
                post.setPicture("/files/" + uploadAddress);
                post.setContent(content);

                // Set as public and save post to the database
                post.setIsPublic(true);
                postRepository.save(post);
            }
        }
        return new RedirectView("/posts");
    }


    @PostMapping("/like/{postId}")
    @ResponseBody
    public void toggleLike(@PathVariable("postId") Long postId) {
        // Fetch the post by its ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post Id"));

        // Get the user_id from the authenticated user
        Long userId = authService.getCurrentUserId();  // Get the user_id using AuthService
        if (userId == null) {
            throw new IllegalArgumentException("User not authenticated");
        }

        // Fetch the user by their user_id (using the ID, not Auth0 ID)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        // Check if the user has already liked the post
        Like existingLike = likeRepository.findByUserIdAndPostId(userId, postId);
        if (existingLike == null) {
            // Create a new like if it doesn't exist
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
        } else {
            // Delete the like if it exists
            likeRepository.delete(existingLike);
        }
    }


    @PostMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable("id") long postId) {
        System.out.println("Attempting to delete post with ID:" +postId);
        postRepository.deleteById(postId);
        return "redirect:/posts";
    }
}
