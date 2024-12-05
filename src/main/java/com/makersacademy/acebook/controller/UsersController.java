package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.FriendRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import com.makersacademy.acebook.service.AuthService;
import com.makersacademy.acebook.service.BlockedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@RestController
public class UsersController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    FriendRepository friendRepository;
    @Autowired
    AuthService authService;
    @Autowired
    BlockedService blockedService;

    @GetMapping("/users/after-login")
    public RedirectView afterLogin() {
        DefaultOidcUser principal = (DefaultOidcUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        String username = (String) principal.getAttributes().get("email");
        userRepository
                .findUserByUsername(username)
                .orElseGet(() -> userRepository.save(new User(username)));

        return new RedirectView("/posts");
    }

    @GetMapping("/profile/{id}")
    public ModelAndView userProfile(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("/user/profile");

        // Get the authenticated user's ID
        Long currentUserId = authService.getCurrentUserId();
        if (currentUserId == null) {
            modelAndView.setViewName("error/403"); // Redirect to "Access Denied" if unauthenticated
            return modelAndView;
        }

        // Get the authenticated user
        User activeUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
        modelAndView.addObject("activeUser", activeUser);

        // Get the user being viewed
        User profileUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        modelAndView.addObject("user", profileUser);

        // Check if blocked
        if (blockedService.isBlocked(currentUserId, id)) {
            if (blockedService.isBlockInitiator(currentUserId, id)) {
                // If the current user initiated the block
                modelAndView.addObject("isBlocked", true);
                modelAndView.setViewName("/user/blocked-profile");
            } else {
                // If blocked by the other user, deny access
                modelAndView.setViewName("error/403");
            }
            return modelAndView;
        }

        // Check friendship status and load posts accordingly
        Optional<String> statusOpt = friendRepository.findRelationshipStatus(currentUserId, id);
        if (statusOpt.isPresent()) {
            String status = statusOpt.get();
            if (status.equals("accepted")) {
                // If friends, show both public and private posts
                Iterable<Post> posts = postRepository.findByUserIdAndIsPublicOrUserId(id, currentUserId);
                modelAndView.addObject("posts", posts);
                return modelAndView;
            }
        }

        // If no relationship exists or status is "pending," show only public posts
        Iterable<Post> posts = postRepository.findByUserIdAndIsPublic(id);
        modelAndView.addObject("posts", posts);

        return modelAndView;
    }

    @PostMapping("/profile/{id}")
    public RedirectView editUser(@ModelAttribute User user) {
        System.out.println(user.getId());
        userRepository.save(user);
        return new RedirectView("/posts");
    }
}
