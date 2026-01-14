package com.example.dockerapi.controller;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.dockerapi.dto.BlogListResponse;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.service.BlogService;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
        private BlogService blogService;
    /*個別にブログ記事を取得・閲覧する */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBlogById(@PathVariable int id) {
        try {
            Blog blog = blogService.getBlogById(id);
            return ResponseEntity.ok(blog);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("エラーが発生しました");
        }
    }

    /*指定のあったページindexをもとにブログ記事を取得する*/
    @GetMapping
    public ResponseEntity<?> getBlogsByCurrentPage(
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "1") int page
    ) {
        try {
            BlogListResponse response = blogService.getBlogList(size, page);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("エラーが発生しました");
        }
    }

    /*新着順に5件ブログ記事を取得する*/
    @GetMapping("/recommend")
    public ResponseEntity<?> getRecentFiveBlogs(){
        try {
            List<Blog> result = blogService.getRecentFiveBlogs();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("エラーが発生しました");
        }
    }

    /*ブログ記事を投稿する*/
    @PostMapping
    public ResponseEntity<?> postNewBlogs(@RequestBody Map<String, String> request) {
        try {
            String title = request.get("title");
            String text = request.get("text");
            Blog newBlog = blogService.postNewBlogs(title, text);
            return ResponseEntity.ok(newBlog);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("エラーが発生しました");
        }
    }

    /*個別にブログ記事を更新する*/
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBlogs(@RequestBody Map<String, String> request, @PathVariable int id) {
        try {
            String title = request.get("title");
            String text = request.get("text");
            int result = blogService.updateBlogs(title, text, id);
            if (result == 0){
                return ResponseEntity.notFound().build();
            }
            /*更新後のブログ記事を取得する*/
            Blog updatedBlog = blogService.getBlogById(id);
            return ResponseEntity.ok(updatedBlog);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("エラーが発生しました");
        }
    }

    /*個別にブログ記事を削除する*/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBlogById(@PathVariable int id) {
        try {
            int result = blogService.deleteBlogById(id);
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
