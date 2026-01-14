package com.example.dockerapi.dto;
import java.util.List;
import com.example.dockerapi.model.Blog;

public class BlogListResponse {

    private List<Blog> blogs;
    private int size;
    private int page;
    private long totalItems;
    private long totalPages;

    public BlogListResponse(
            List<Blog> blogs,
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

    public List<Blog> getBlogs() {
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