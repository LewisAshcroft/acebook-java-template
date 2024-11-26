package com.makersacademy.acebook.model;

import jakarta.persistence.*;

import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String picture;
    private Long user_id;
    private Boolean is_public;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Post() {}

    public Post(String content, String picture, Long user_id, Boolean is_public, Timestamp created_at, Timestamp updated_at) {
        this.content = content;
        this.picture = picture;
        this.user_id = user_id;
        this.is_public = is_public;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getContent() { return this.content; }
    public void setContent(String content) { this.content = content; }
    public String getPicture() { return this.picture; }
    public void setPicture(String picture) { this.picture = picture; }
}
