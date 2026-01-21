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
import com.example.dockerapi.dto.BlogRequest;
import com.example.dockerapi.dto.CommentRequest;
import com.example.dockerapi.dto.PagedResponse;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.model.Comment;
import com.example.dockerapi.model.Ranking;
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
    public ResponseEntity<ApiResponse<PagedResponse<Blog>>> getBlogsByCurrentPage(
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "1") int page
    ) {
        try {
            PagedResponse<Blog> response = blogService.getBlogList(size, page);
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

    /*指定したブログ記事のコメント一覧を取得する*/
    @GetMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<PagedResponse<Comment>>> getCommentsOfTheBlog(
        @PathVariable int id,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "1") int page
    ) {
        try {
            PagedResponse<Comment> response = blogService.getCommentsOfTheBlog(id, size, page);
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*指定したブログ記事にコメントを投稿する*/
    @PostMapping("/{id}/comments")
    public ResponseEntity<ApiResponse<List<Comment>>> postNewComment(
        @RequestBody CommentRequest request, 
        @PathVariable int id
    ) { 
        try {
            String text = request.getText();
            String creator = request.getCreatedBy();
            List<Comment> newCommentList = blogService.postNewComment(id, text, creator);
            return ResponseEntity.ok(ApiResponse.success(newCommentList));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*指定したコメントを更新する*/
    @PutMapping("/{id}/comments/{commentId}")
    public ResponseEntity<ApiResponse<String>> updateComment(@RequestBody CommentRequest request, @PathVariable int commentId) {
        try {
            String text = request.getText();
            //String creator = request.getCreatedBy();
            int result = blogService.updateComment(text, commentId);
            if (result == 0){
                return ResponseEntity.status(404).body(ApiResponse.error("ブログが見つかりません"));
            }
            
            return ResponseEntity.ok(ApiResponse.success("コメントを編集しました"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*指定したコメントを削除する*/
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<ApiResponse<List<Comment>>> deleteCommentById(@PathVariable int id, @PathVariable int commentId) {
        try {
            List<Comment> comment = blogService.deleteCommentById(id, commentId);
            return ResponseEntity.ok(ApiResponse.success(comment));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*ブログ記事のランキングを取得する*/
    @GetMapping("/ranking")
    public ResponseEntity<ApiResponse<List<Ranking>>> getRankingOfTheBlog() {
        try {
            List<Ranking> ranking = blogService.getRankingOfTheBlog();
            return ResponseEntity.ok(ApiResponse.success(ranking));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ApiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }
}