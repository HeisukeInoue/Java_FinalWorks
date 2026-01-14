package com.example.dockerapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dockerapi.dto.blogListResponse;
import com.example.dockerapi.model.blog;
import com.example.dockerapi.repository.blogRepository;

@Service
public class blogService {
    
    @Autowired
    private blogRepository blogRepository;

    public blog getblogById(int id) {
        return blogRepository.findById(id);
    }

    public blogListResponse getblogList(int size, int page) {
        int offset = (page - 1) * size;
        List<blog> blogs = blogRepository.getblogsByCurrentPage(size, offset);
        long totalItems = blogRepository.getTotalblogCounts();
        long totalPages = totalItems / size;
        if (totalItems % size != 0) {
            totalPages++;
        }
        return new blogListResponse(blogs, page, size, totalItems, totalPages);
    }

    public List<blog> getRecentFiveblogs() {
        return blogRepository.getRecentFiveblogs();
    }
    
    public blog postNewblogs(String blogtitle, String blogtext) {
        return blogRepository.postNewblogs(blogtitle, blogtext);
    }

    public int updateblogs(String blogtitle, String blogtext, int blogid) {
        return blogRepository.updateblogs(blogtitle, blogtext, blogid);
    }

    public int deleteblogById(int id) {
        return blogRepository.deleteById(id);
    }
}
