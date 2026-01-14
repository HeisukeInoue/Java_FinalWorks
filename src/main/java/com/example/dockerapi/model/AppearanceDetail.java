package com.example.dockerapi.model;
import java.sql.Date;
import java.time.LocalDateTime;

public class AppearanceDetail extends Appearance{
    private String text;

    public AppearanceDetail(int id, int talentId, Date date, String title, String text,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(id, talentId, date, title, createdAt, updatedAt);
        this.text = text;
    }
    public String getText() {
        return text;
    }
}
