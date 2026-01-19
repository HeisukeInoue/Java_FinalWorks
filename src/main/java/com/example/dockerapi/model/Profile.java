package com.example.dockerapi.model;
import java.time.LocalDateTime;

public class Profile {
    private int id;
    private String image_link;
    private String name;
    private int age;
    private String from;
    private int height;
    private String hobby;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Profile(int id, String image_link, String name, int age, String from, int height, String hobby,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.image_link = image_link;
        this.name = name;
        this.age = age;
        this.from = from;
        this.height = height;
        this.hobby = hobby;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { 
        return id; 
    }
    public String getImageLink() {
        return image_link;
    }
    public String getName() {
        return name; 
    }

    public int getAge() { 
        return age; 
    }

    public String getFrom() { 
        return from; 
    }

    public int getHeight() { 
        return height; 
    }

    public String getHobby() {
        return hobby;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt; 
    }
    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
}