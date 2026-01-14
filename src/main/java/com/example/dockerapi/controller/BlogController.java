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
import com.example.dockerapi.dto.apiResponse;
import com.example.dockerapi.dto.blogListResponse;
import com.example.dockerapi.dto.blogRequest;
import com.example.dockerapi.model.blog;
import com.example.dockerapi.service.blogService;

@RestController
@RequestMapping("/api/blogs")
public class blogController {

    @Autowired
        private blogService blogService;
    /*個別にブログ記事を取得・閲覧する */
    @GetMapping("/{id}")
    public ResponseEntity<apiResponse<blog>> getblogById(@PathVariable int id) {
        try {
            blog blog = blogService.getblogById(id);
            if (blog == null) {
                return ResponseEntity.status(404)
                    .body(apiResponse.error("ブログが見つかりません"));
            }
            return ResponseEntity.ok(apiResponse.success(blog));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(apiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }
    
    /*指定のあったページindexをもとにブログ記事を取得する*/
    @GetMapping
    public ResponseEntity<apiResponse<blogListResponse>> getblogsByCurrentPage(
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "1") int page
    ) {
        try {
            blogListResponse response = blogService.getblogList(size, page);
            return ResponseEntity.ok(apiResponse.success(response));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(apiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*新着順に5件ブログ記事を取得する*/
    @GetMapping("/recommend")
    public ResponseEntity<apiResponse<List<blog>>> getRecentFiveblogs(){
        try {
            List<blog> result = blogService.getRecentFiveblogs();
            return ResponseEntity.ok(apiResponse.success(result));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(apiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*ブログ記事を投稿する*/
    @PostMapping
    public ResponseEntity<apiResponse<blog>> postNewblogs(@RequestBody blogRequest request) {
        try {
            String title = request.getTitle();
            String text = request.getText();
            blog newblog = blogService.postNewblogs(title, text);
            return ResponseEntity.ok(apiResponse.success(newblog));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(apiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*個別にブログ記事を更新する*/
    @PutMapping("/{id}")
    public ResponseEntity<apiResponse<blog>> updateblogs(@RequestBody blogRequest  request, @PathVariable int id) {
        try {
            String title = request.getTitle();
            String text = request.getText();
            int result = blogService.updateblogs(title, text, id);
            if (result == 0){
                return ResponseEntity.status(404).body(apiResponse.error("ブログが見つかりません"));
            }
            /*更新後のブログ記事を取得する*/
            blog updatedblog = blogService.getblogById(id);
            return ResponseEntity.ok(apiResponse.success(updatedblog));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(apiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    /*個別にブログ記事を削除する*/
    @DeleteMapping("/{id}")
    public ResponseEntity<apiResponse<Void>> deleteblogById(@PathVariable int id) {
        try {
            int result = blogService.deleteblogById(id);
            if (result == 0){
                return ResponseEntity.status(404).body(apiResponse.error("ブログが見つかりません"));
            }
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(apiResponse.error("エラーが発生しました: " + e.getMessage()));
        }
    }

    
}
