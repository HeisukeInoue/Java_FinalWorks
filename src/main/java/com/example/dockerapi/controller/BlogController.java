package com.example.dockerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    /*個別にブログ記事を取得・閲覧する */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable int id) {
        try {
            Blog blog = blogRepository.findById(id);
            return ResponseEntity.ok(blog);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("エラーが発生しました");
        }
    }

    /*個別にブログ記事を削除する*/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlogById(@PathVariable int id) {
        try {
            int result = blogRepository.deleteById(id);
            if (result == 0){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("エラーが発生しました");
        }
    }
}
