package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.Post;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.FriendRepository;
import com.makersacademy.acebook.repository.PostRepository;
import com.makersacademy.acebook.repository.UserRepository;
import com.makersacademy.acebook.service.AuthService;
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
    public ModelAndView userProfile(@PathVariable Long id, Model model) {
        ModelAndView modelAndView = new ModelAndView("/user/profile");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> currentUser = userRepository.findByAuth0Id(auth.getName());
        if (currentUser.isPresent()) {
            User activeUser = currentUser.get();
            modelAndView.addObject("activeUser", activeUser);
        }

        User user = userRepository.findById(id).get();
        modelAndView.addObject("user", user);

        Iterable<Post> posts = postRepository.findAllByUserId(id);
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
