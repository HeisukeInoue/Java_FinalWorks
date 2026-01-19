package com.example.dockerapi.dto;

public class ProfileRequest {
    private String image_link;
    private String name;
    private int age;
    private String from;
    private int height;
    private String hobby;

    public ProfileRequest(String image_link, String name, int age, String from, int height, String hobby) {
        this.image_link = image_link;
        this.name = name;
        this.age = age;
        this.from = from;
        this.height = height;
        this.hobby = hobby;
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
}