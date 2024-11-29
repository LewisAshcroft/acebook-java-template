package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Like;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.LikeRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import com.makersacademy.acebook.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = userRepository.findByAuth0Id(auth.getName());
        if (currentUser.isPresent()) {
            User activeUser = currentUser.get();
            Post post = new Post("", "", activeUser.getId(), false, null, null);
            String uploadAddress = storageService.save(file);
            post.setPicture(uploadAddress);
            post.setContent(content);
            postRepository.save(post);
        }
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


    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") long postId) {
        postRepository.deleteById(postId);
        return "redirect:/post";
    }
}
