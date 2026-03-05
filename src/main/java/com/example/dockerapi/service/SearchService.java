package com.example.dockerapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.repository.SearchRepository;

@Service
public class SearchService {
    
    @Autowired
    private SearchRepository searchRepository;

    public List<Blog> searchBlogs(String query) {
        return searchRepository.searchBlogs(query.trim());       
    }
}
