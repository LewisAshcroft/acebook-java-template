package com.makersacademy.acebook.model;

import jakarta.persistence.*;

import lombok.Data;
import org.hibernate.type.descriptor.jdbc.TimestampWithTimeZoneJdbcType;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "POSTS")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String picture;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "is_public")
    private Boolean isPublic;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Post() {}

    public Post(String content, String picture, Long userId, Boolean isPublic, Timestamp createdAt, Timestamp updatedAt) {
        this.content = content;
        this.picture = picture;
        this.userId = userId;
        this.isPublic = isPublic;
        this.createdAt = (createdAt != null) ? createdAt : Timestamp.valueOf(LocalDateTime.now());
        this.updatedAt = (updatedAt != null) ? updatedAt : Timestamp.valueOf(LocalDateTime.now());
    }

    public String getContent() { return this.content; }
    public void setContent(String content) { this.content = content; }
    public String getPicture() { return this.picture; }
    public void setPicture(String picture) { this.picture = picture; }
    public Long getId() {return this.id;}
    public void setId() { this.id = id;}
    public Long getUserId() { return this.userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Boolean getIsPublic() { return this.isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }
    public Timestamp getCreatedAt() { return this.createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return this.updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }



}
