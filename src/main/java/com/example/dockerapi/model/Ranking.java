package com.example.dockerapi.model;
import java.time.LocalDateTime;

public class Ranking{
    private int id;
    private int rank;
    private int blogId;
    private int commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    
    public Ranking(int id, int rank, int blogId, int commentCount, 
                LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.rank = rank;
        this.blogId = blogId;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public int getId() { 
        return id; 
    }
    public int getRank() {
        return rank;
    }
    public int getBlogId() {
        return blogId;
    }
    public int getCommentCount() {
        return commentCount;
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