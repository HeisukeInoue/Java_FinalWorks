package com.example.dockerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.repository.BlogRepository;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
        private BlogRepository blogRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable int id) {
        try {
            Blog blog = blogRepository.findById(id);
            return ResponseEntity.ok(blog);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
