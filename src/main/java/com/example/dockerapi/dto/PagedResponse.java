package com.example.dockerapi.dto;
import java.util.List;

public class PagedResponse<T> {
    private List<T> items;
    private int size;
    private int page;
    private long totalItems;
    private long totalPages;

    public PagedResponse(
            List<T> items,
            int page,
            int size,
            long totalItems,
            long totalPages
    ) {
        this.items = items;
        this.size = size;
        this.page = page;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }

    public List<T> getItems() {
        return items;
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