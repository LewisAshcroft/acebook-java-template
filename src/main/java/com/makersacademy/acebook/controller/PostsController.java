package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Like;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.LikeRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import com.makersacademy.acebook.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class PostsController {

    @Autowired
    PostRepository postRepository;
    @Autowired
    FilesStorageService storageService;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    UserRepository userRepository;  // Assuming you have a User repository

    @GetMapping("/posts")
    public String index(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        return "posts/index";
    }

    @GetMapping("/new-post")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());
        return "posts/new-post";
    }

    @PostMapping("/new-post")
    public RedirectView create(@RequestParam("file") MultipartFile file, @RequestParam("content") String content) {
        Post post = new Post("", "", 1L, false, null, null);
        String uploadAddress = storageService.save(file);
        post.setPicture(uploadAddress);
        post.setContent(content);
        postRepository.save(post);
        return new RedirectView("/posts");
    }




    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable("postId") Long postId, @RequestParam("userId") Long userId) {

        // Fetch the post and user by their IDs
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post Id"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id"));

        // Check if the user has already liked the post
        Like existingLike = likeRepository.findByUserIdAndPostId(userId, postId);
        if (existingLike == null) {
            // Create a new like if it doesn't exist
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
        }

        // Redirect to the posts page (or wherever you want)
        return "redirect:/posts";  // Ensure this path is correct
    }



    // Unlike a post
    @DeleteMapping("/unlike/{postId}")
    public String unlikePost(@PathVariable("postId") Long postId, @RequestParam("userId") Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Invalid post Id"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id"));

        // Find and delete the like
        Like existingLike = likeRepository.findByUserIdAndPostId(userId, postId);
        if (existingLike != null) {
            likeRepository.delete(existingLike);
        }

        // Redirect to the posts page (or wherever you want)
        return "redirect:/posts";
    }
}
