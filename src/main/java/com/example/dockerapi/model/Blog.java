package com.example.dockerapi.model;
import java.time.LocalDateTime;

public class blog {
    private int id;
    private int talentId;
    private String title;
    private String text;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public blog(int id, int talentId, String title, String text,
                LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.talentId = talentId;
        this.title = title;
        this.text = text;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public int getId() { 
        return id; 
    }
    public int getTalentId() {
        return talentId;
    }
    public String getTitle() {
        return title; 
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
    public LocalDateTime getDeletedAt() { 
        return deletedAt; 
    }
}