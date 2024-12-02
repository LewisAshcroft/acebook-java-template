package com.makersacademy.acebook.service;

import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof DefaultOidcUser) {
                DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
                String auth0Id = oidcUser.getSubject();  // Get the Auth0 ID (subject)

                // Look up the user by their auth0_id in the database
                User user = userRepository.findByAuth0Id(auth0Id)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

                return user.getId();  // Return the user's database id (user_id)
            }
        }
        return null;  // If no user is authenticated, return null
    }
}
