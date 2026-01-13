package com.example.dockerapi.model;
import java.sql.Date;
import java.time.LocalDateTime;

public class Appearance {
    private int id;
    private int talentId;
    private Date date;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Appearance(int id, int talentId, Date date, String title,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.talentId = talentId;
        this.date = date;
        this.title = title;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
    public Date getdate() {
        return date; 
    }
    public LocalDateTime getCreatedAt() {
        return createdAt; 
    }
    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
}
