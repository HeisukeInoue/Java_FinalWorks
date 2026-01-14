package com.example.dockerapi.controller;
import java.util.List;
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
import com.example.dockerapi.dto.ApiResponse;
import com.example.dockerapi.dto.BlogListResponse;
import com.example.dockerapi.dto.BlogRequest;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.service.BlogService;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    @Autowired
        private BlogService blogService;
    /*個別にブログ記事を取得・閲覧する */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Blog>> getBlogById(@PathVariable int id) {
        try {
            Blog blog = blogService.getBlogById(id);
            if (blog == null) {
                return ResponseEntity.status(404)
                    .body(ApiResponse.error("ブログが見つかりません"));
            }
            return ResponseEntity.ok(ApiResponse.success(blog));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }
    
    /*指定のあったページindexをもとにブログ記事を取得する*/
    @GetMapping
    public ResponseEntity<ApiResponse<BlogListResponse>> getBlogsByCurrentPage(
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "1") int page
    ) {
        try {
            BlogListResponse response = blogService.getBlogList(size, page);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*新着順に5件ブログ記事を取得する*/
    @GetMapping("/recommend")
    public ResponseEntity<ApiResponse<List<Blog>>> getRecentFiveBlogs(){
        try {
            List<Blog> result = blogService.getRecentFiveBlogs();
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*ブログ記事を投稿する*/
    @PostMapping
    public ResponseEntity<ApiResponse<Blog>> postNewBlogs(@RequestBody BlogRequest request) {
        try {
            String title = request.getTitle();
            String text = request.getText();
            Blog newBlog = blogService.postNewBlogs(title, text);
            return ResponseEntity.ok(ApiResponse.success(newBlog));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*個別にブログ記事を更新する*/
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Blog>> updateBlogs(@RequestBody BlogRequest  request, @PathVariable int id) {
        try {
            String title = request.getTitle();
            String text = request.getText();
            int result = blogService.updateBlogs(title, text, id);
            if (result == 0){
                return ResponseEntity.status(404).body(ApiResponse.error("ブログが見つかりません"));
            }
            /*更新後のブログ記事を取得する*/
            Blog updatedBlog = blogService.getBlogById(id);
            return ResponseEntity.ok(ApiResponse.success(updatedBlog));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*個別にブログ記事を削除する*/
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBlogById(@PathVariable int id) {
        try {
            int result = blogService.deleteBlogById(id);
            if (result == 0){
                return ResponseEntity.status(404).body(ApiResponse.error("ブログが見つかりません"));
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    } 
}
