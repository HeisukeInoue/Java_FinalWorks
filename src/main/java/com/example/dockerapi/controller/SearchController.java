package com.example.dockerapi.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.dockerapi.dto.ApiResponse;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.service.SearchService;


@RestController
@RequestMapping("/api")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Blog>>> searchBlogs(@RequestParam String query) {
        try {
            List<Blog> blogs = searchService.searchBlogs(query);
            return ResponseEntity.ok(ApiResponse.success(blogs));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }
}
