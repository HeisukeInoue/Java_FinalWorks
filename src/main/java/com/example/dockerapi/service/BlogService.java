package com.example.dockerapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dockerapi.dto.PagedResponse;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.model.Comment;
import com.example.dockerapi.repository.BlogRepository;

@Service
public class BlogService {
    
    @Autowired
    private BlogRepository blogRepository;

    public Blog getBlogById(int id) {
        return blogRepository.findById(id);
    }

    public PagedResponse<Blog> getBlogList(int size, int page) {
        int offset = (page - 1) * size;
        List<Blog> blogs = blogRepository.getBlogsByCurrentPage(size, offset);
        long totalItems = blogRepository.getTotalBlogCounts();
        long totalPages = totalItems / size;
        if (totalItems % size != 0) {
            totalPages++;
        }
        return new PagedResponse<>(blogs, page, size, totalItems, totalPages);
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

    /*指定したブログ記事のコメント一覧を取得する*/
    public PagedResponse<Comment> getCommentsOfTheBlog(int id, int size, int page) {
        int offset = (page - 1) * size;
        List<Comment> comments = blogRepository.getCommentsOfTheBlog(id, size, offset);
        long totalItems = blogRepository.getTotalCommentCounts(id);
        long totalPages = totalItems / size;
        if (totalItems % size != 0) {
            totalPages++;
        }
        return new PagedResponse<>(comments, page, size, totalItems, totalPages);
    }

    /*指定したブログ記事にコメントを投稿する*/
    public List<Comment> postNewComment(int id, String text, String creator) {
        return blogRepository.postNewComment(id, text, creator);
    }

    /*指定したコメントを更新する*/
    public int updateComment(String text, int commentId) {
        return blogRepository.updateComment(text, commentId);
    }

    /*指定したコメントを削除する*/
    public List<Comment> deleteCommentById(int id, int comment_id) {
        return blogRepository.deleteCommentById(id, comment_id);
    }

    /*ブログ記事のランキングを取得する*/
    public List<Blog> getRankingOfTheBlog() {
        return blogRepository.getRankingOfTheBlog();
    }
}
