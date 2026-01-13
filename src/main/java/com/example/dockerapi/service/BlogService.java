package com.example.dockerapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dockerapi.dto.BlogListResponse;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.repository.BlogRepository;

@Service
public class BlogService {
    
    @Autowired
    private BlogRepository blogRepository;

    public Blog getBlogById(int id) {
        return blogRepository.findById(id);
    }

    public BlogListResponse getBlogList(int size, int page) {
        int offset = (page - 1) * size;
        List<Blog> blogs = blogRepository.getBlogsByCurrentPage(size, offset);
        long totalItems = blogRepository.getTotalBlogCounts();
        long totalPages = totalItems / size;
        if (totalItems % size != 0) {
            totalPages++;
        }
        return new BlogListResponse(blogs, page, size, totalItems, totalPages);

    }
    
    public Blog postNewBlogs(String blogtitle, String blogtext) {
        return blogRepository.postNewBlogs(blogtitle, blogtext);
    }

    public int updateBlogs(String blogtitle, String blogtext, int blogid) {
        return blogRepository.updateBlogs(blogtitle, blogtext, blogid);
    }

    public int deleteBlogById(int id) {
        return blogRepository.deleteById(id);
    }
}
