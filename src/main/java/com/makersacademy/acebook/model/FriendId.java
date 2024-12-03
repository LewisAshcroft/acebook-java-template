package com.makersacademy.acebook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FriendId implements Serializable {

    @Column(name = "user_1_id")
    private Long user1Id;

    @Column(name = "user_2_id")
    private Long user2Id;

    // Constructors
    public FriendId() {
    }

    public FriendId(Long user1Id, Long user2Id) {
        this.user1Id = user1Id;
        this.user2Id = user2Id;
    }

    // Getters and Setters
    public Long getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(Long user1Id) {
        this.user1Id = user1Id;
    }

    public Long getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(Long user2Id) {
        this.user2Id = user2Id;
    }

    // Overrides for hashCode and equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendId friendId = (FriendId) o;
        return Objects.equals(user1Id, friendId.user1Id) &&
                Objects.equals(user2Id, friendId.user2Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1Id, user2Id);
    }
}
