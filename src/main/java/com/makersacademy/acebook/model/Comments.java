package com.makersacademy.acebook.model;

import jakarta.persistence.*;

import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "Comments")
public class Comments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long post_id;
    private Long user_id;
    private String content;
    private Timestamp created_at;

    public Comments() {}

    public Comments(Long post_id,Long user_id,String content, Timestamp created_at) {
        this.content = content;
        this.created_at = created_at;
        this.user_id = user_id;
        this.post_id = post_id;

    }
    public Long getId() {return this.id = id;}
    public void setId() { this.id = id;}
    public Long getPost_id() {return this.post_id;}
    public void setPost_id(Long post_id) { this.post_id = post_id; }
    public Long getUser_id() {return this.user_id;}
    public void setUser_id(Long user_id){this.user_id = user_id;}
    public String getContent() { return this.content; }
    public void setContent(String content) { this.content = content; }
    public Timestamp getCreated_at() { return this.created_at; }
    public void setCreated_at(String picture) { this.created_at = created_at; }
}