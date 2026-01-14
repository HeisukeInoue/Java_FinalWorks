/*出演情報投稿&更新リクエストボディ用のDTO*/
package com.example.dockerapi.dto;
import java.sql.Date;

public class AppearanceRequest {
    private Date date;
    private String title;
    private String text;

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
