package com.makersacademy.acebook.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(name = "auth0_id")
    private String auth0Id;

    private String email;

    private String firstName;

    private String middleName;

    private String lastName;

    private String bio;

    private boolean enabled;

    public User() {
        this.enabled = true;
    }

    // Constructor with username only
    public User(String username) {
        this.username = username;
        this.enabled = true;
    }

    // Constructor with username and enabled status
    public User(String username, boolean enabled) {
        this.username = username;
        this.enabled = enabled;
    }

    public User(String username, String bio) {
        this.username = username;
        this.bio = bio;
    }

    // Getters and Setters
    public String getAuth0Id() {
        return auth0Id;
    }

    public void setAuth0Id(String auth0Id) {
        this.auth0Id = auth0Id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
