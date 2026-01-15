package com.example.dockerapi.model;
import java.time.LocalDateTime;

public class Comment {
    private int id;
    private int blog_id;
    private String created_by;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Comment(int id, int blog_id, String created_by, String text,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.blog_id = blog_id;
        this.created_by = created_by;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { 
        return id; 
    }
    public int getBlogId() {
        return blog_id;
    }
    public String getCreator(){
        return created_by; 
    }
    public String getText() { 
        return text; 
    }
    public LocalDateTime getCreatedAt() {
        return createdAt; 
    }
    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
}