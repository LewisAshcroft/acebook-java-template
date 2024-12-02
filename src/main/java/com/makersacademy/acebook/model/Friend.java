package com.makersacademy.acebook.model;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "friends")
public class Friend {

    @EmbeddedId
    private FriendId id;

    @Column(nullable = false)
    private String status;

    // Constructors
    public Friend() {
    }

    public Friend(Long user1Id, Long user2Id, String status) {
        this.id = new FriendId(user1Id, user2Id);
        this.status = status;
    }

    public Friend(FriendId friendId, String status) {
        this.id = friendId;
        this.status = status;
    }

    // Getters and Setters
    public FriendId getId() {
        return id;
    }

    public void setId(FriendId id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
