package com.example.dockerapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.repository.BlogRepository;

@Service
public class BlogService {
    
    @Autowired
    private BlogRepository blogRepository;

    public Blog getBlogById(int id) {
        return blogRepository.findById(id);
    }

    public int deleteBlogById(int id) {
        return blogRepository.deleteById(id);
    }
}
