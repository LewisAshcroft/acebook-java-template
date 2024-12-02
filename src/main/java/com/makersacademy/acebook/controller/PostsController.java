package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Like;
import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.LikeRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import com.makersacademy.acebook.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

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

    @GetMapping("/posts")
    public String index(Model model) {
        // Retrieve all posts
        Iterable<Post> posts = postRepository.findAll();

        // Get the authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = userRepository.findByAuth0Id(auth.getName());

        // Map posts and attach "isLiked" status and like count
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

            // Add the like count
            long likeCount = likeRepository.countByPostId(post.getId());
            postWithStatus.put("likeCount", likeCount > 0 ? likeCount : 0);

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = userRepository.findByAuth0Id(auth.getName());
        if (currentUser.isPresent()) {
            User activeUser = currentUser.get();
            Post post = new Post("", "", activeUser.getId(), false, null, null);
            String uploadAddress = storageService.save(file);
            post.setPicture("/files/" + uploadAddress);
            post.setContent(content);
            postRepository.save(post);
        }
        return new RedirectView("/posts");
    }


    @PostMapping("/like/{postId}")
    @ResponseBody
    public Map<String, Object> likePost(@PathVariable("postId") Long postId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = userRepository.findByAuth0Id(auth.getName());

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = userRepository.findByAuth0Id(auth.getName());

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



    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") long postId) {
        postRepository.deleteById(postId);
        return "redirect:/post";
    }
}
