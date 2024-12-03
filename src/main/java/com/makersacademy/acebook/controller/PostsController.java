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

    @GetMapping("/posts")
    public String index(Model model) {
        // Retrieve all posts
        Iterable<Post> posts = postRepository.findAll();

        // Get the authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = userRepository.findByAuth0Id(auth.getName());

        // Map posts and attach "isLiked" status
        List<Map<String, Object>> postsWithLikeStatus = new ArrayList<>();
        for (Post post : posts) {
            Map<String, Object> postWithStatus = new HashMap<>();
            postWithStatus.put("post", post);

            if (currentUser.isPresent()) {
                User user = currentUser.get();
                boolean isLiked = likeRepository.findByUserIdAndPostId(user.getId(), post.getId()) != null;
                postWithStatus.put("isLiked", isLiked);
            } else {
                postWithStatus.put("isLiked", false);
            }
            postsWithLikeStatus.add(postWithStatus);
        }

        model.addAttribute("postsWithLikeStatus", postsWithLikeStatus);
        return "posts/index";
    }

    @GetMapping("/new-post")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        return "posts/new-post";
    }

    @PostMapping("/new-post")
    public RedirectView create(@RequestParam("file") MultipartFile file, @RequestParam("content") String content) {
        Long userId = authService.getCurrentUserId(); // Get the currently logged-in user's ID

        if (userId != null) {
            Optional<User> currentUser = userRepository.findById(userId); // Find the user by user_id

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
    public String likePost(@PathVariable("postId") Long postId) {
        // Retrieve the authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = userRepository.findByAuth0Id(auth.getName());

        if (currentUser.isEmpty()) {
            return "redirect:/posts"; // Redirect if user is not authenticated
        }

        User user = currentUser.get();
        Like existingLike = likeRepository.findByUserIdAndPostId(user.getId(), postId);

        // Add a like if not already liked
        if (existingLike == null) {
            Like like = new Like();
            like.setPost(postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post Id")));
            like.setUser(user);
            likeRepository.save(like);
        }

        return "redirect:/posts";
    }

    @DeleteMapping("/unlike/{postId}")
    public String unlikePost(@PathVariable("postId") Long postId) {
        // Retrieve the authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = userRepository.findByAuth0Id(auth.getName());

        if (currentUser.isEmpty()) {
            return "redirect:/posts"; // Redirect if unauthenticated
        }

        User user = currentUser.get();
        Like existingLike = likeRepository.findByUserIdAndPostId(user.getId(), postId);

        // Remove the like if it exists
        if (existingLike != null) {
            likeRepository.delete(existingLike);
        }

        return "redirect:/posts";
    }

    @PostMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable("id") long postId) {
        System.out.println("Attempting to delete post with ID:" + postId);
        postRepository.deleteById(postId);
        return "redirect:/posts";
    }
}
