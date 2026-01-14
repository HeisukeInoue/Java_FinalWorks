package com.example.dockerapi.dto;
import java.util.List;
import com.example.dockerapi.model.blog;

public class blogListResponse {

    private List<blog> blogs;
    private int size;
    private int page;
    private long totalItems;
    private long totalPages;

    public blogListResponse(
            List<blog> blogs,
            int page,
            int size,
            long totalItems,
            long totalPages
    ) {
        this.blogs = blogs;
        this.size = size;
        this.page = page;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    public List<blog> getblogs() {
        return blogs;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public long getTotalPages() {
        return totalPages;
    }
}