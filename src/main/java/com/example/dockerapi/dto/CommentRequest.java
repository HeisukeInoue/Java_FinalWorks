package com.example.dockerapi.dto;

public class CommentRequest {
    private String text;
    private String createdBy;

    public String getText() {
        return text;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}