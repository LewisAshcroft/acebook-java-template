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
    private Integer user_id;
    private Boolean is_public;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Post() {}

    public Post(String content) {
        this.content = content;
        this.picture = "temp";
        this.user_id = 1;
        this.is_public = false;
        this.created_at = new Timestamp(1);
        this.updated_at = new Timestamp(1);
    }
    public String getContent() { return this.content; }
    public void setContent(String content) { this.content = content; }

}
