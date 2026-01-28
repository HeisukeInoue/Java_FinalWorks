package com.example.dockerapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.dockerapi.dto.PagedResponse;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.model.Comment;
import com.example.dockerapi.repository.BlogRepository;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogService blogService;

    /*個別にブログ記事を取得・閲覧する 正常系テスト*/
    @Test
    void testGetBlogById() {
        Blog dummyBlog = new Blog(
            1, 
            1, 
            "テストタイトル", 
            "テスト本文", 
            LocalDateTime.now(), 
            null, 
            null
        );
        when(blogRepository.findById(1)).thenReturn(dummyBlog);

        Blog result = blogService.getBlogById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getTalentId());
        assertEquals("テストタイトル", result.getTitle());
        assertEquals("テスト本文", result.getText());
    }

    /*個別にブログ記事を取得・閲覧する 異常系テスト*/
    @Test
    void testGetBlogById_error() {
        when(blogRepository.findById(999)).thenReturn(null);
        Blog result = blogService.getBlogById(999);

        assertNull(result);
    }

    /*ブログ記事リストを取得する 正常系テスト*/
    @Test
    void testGetBlogList_success() {
        List<Blog> mockBlogs = new ArrayList<>();
        mockBlogs.add(new Blog(1, 1, "タイトル1", "本文1", LocalDateTime.now(), null, null));
        mockBlogs.add(new Blog(2, 1, "タイトル2", "本文2", LocalDateTime.now(), null, null));
        mockBlogs.add(new Blog(3, 1, "タイトル3", "本文3", LocalDateTime.now(), null, null));
        
        long totalItems = 3L;  // 総件数
        int size = 10;
        int page = 1;
        int offset = (page - 1) * size;  // 0

        when(blogRepository.getBlogsByCurrentPage(size, offset)).thenReturn(mockBlogs);
        when(blogRepository.getTotalBlogCounts()).thenReturn(totalItems);

        PagedResponse<Blog> result = blogService.getBlogList(size, page);

        assertNotNull(result);
        assertEquals(3, result.getItems().size());
        assertEquals(page, result.getPage());
        assertEquals(size, result.getSize());
        assertEquals(totalItems, result.getTotalItems());
        assertEquals(1, result.getTotalPages());
    }

    /*ブログ記事リストを取得する 正常系テスト（2ページ目）*/
    @Test
    void testGetBlogList_success_page2() {
        List<Blog> mockBlogs = new ArrayList<>();
        mockBlogs.add(new Blog(11, 1, "タイトル11", "本文11", LocalDateTime.now(), null, null));
        mockBlogs.add(new Blog(12, 1, "タイトル12", "本文12", LocalDateTime.now(), null, null));
        
        long totalItems = 25L;
        int size = 10;
        int page = 2;
        int offset = (page - 1) * size;  // 10

        when(blogRepository.getBlogsByCurrentPage(size, offset)).thenReturn(mockBlogs);
        when(blogRepository.getTotalBlogCounts()).thenReturn(totalItems);

        PagedResponse<Blog> result = blogService.getBlogList(size, page);

        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        assertEquals(page, result.getPage());
        assertEquals(size, result.getSize());
        assertEquals(totalItems, result.getTotalItems());
        assertEquals(3, result.getTotalPages());
    }

    /*ブログ記事リストを取得する 異常系テスト（存在しないページ番号）*/
    @Test
    void testGetBlogList_error() {
        List<Blog> mockBlogs = new ArrayList<>();
        long totalItems = 25L;
        int size = 10;
        int page = 999;
        int offset = (page - 1) * size;

        when(blogRepository.getBlogsByCurrentPage(size, offset)).thenReturn(mockBlogs);
        when(blogRepository.getTotalBlogCounts()).thenReturn(totalItems);
        PagedResponse<Blog> result = blogService.getBlogList(size, page);

        assertNotNull(result);
        assertEquals(0, result.getItems().size());
        assertEquals(page, result.getPage());
        assertEquals(size, result.getSize());
        assertEquals(totalItems, result.getTotalItems());
        assertEquals(3, result.getTotalPages());  // 実際の総ページ数
    }

    /*ブログ記事を投稿する 正常系テスト*/
    @Test
    void testPostNewBlogs() {
        Blog dummyBlog = new Blog(
            1, 
            1, 
            "テストタイトル", 
            "テスト本文", 
            LocalDateTime.now(), 
            null, 
            null
        );
        when(blogRepository.postNewBlogs("テストタイトル", "テスト本文")).thenReturn(dummyBlog);
        Blog result = blogService.postNewBlogs("テストタイトル", "テスト本文");

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getTalentId());
        assertEquals("テストタイトル", result.getTitle());
        assertEquals("テスト本文", result.getText());
    }

    /*ブログ記事を投稿する 異常系テスト*/
    @Test
    void testPostNewBlogs_error() {
        when(blogRepository.postNewBlogs("テストタイトル", "テスト本文")).thenReturn(null);
        Blog result = blogService.postNewBlogs("テストタイトル", "テスト本文");

        assertNull(result);
    }

    /*個別にブログ記事を更新する 正常系テスト*/
    @Test
    void testUpdateBlogs() {
        int expectedResult = 1;
        when(blogRepository.updateBlogs ("更新後のテストタイトル", "更新後のテスト本文", 1)).thenReturn(expectedResult);
        int result = blogService.updateBlogs("更新後のテストタイトル", "更新後のテスト本文", 1);
        assertEquals(expectedResult, result);
    }

    /*個別にブログ記事を更新する 異常系テスト*/
    @Test
    void testUpdateBlogs_error() {
        int expectedResult = 0;
        when(blogRepository.updateBlogs("テストタイトル", "テスト本文", 999)).thenReturn(expectedResult);
        int result = blogService.updateBlogs("テストタイトル", "テスト本文", 999);

        assertEquals(expectedResult, result);
    }

    /*ブログ記事を削除する 正常系テスト*/
    @Test
    void testDeleteBlogById() {
        int expectedResult = 1;
        when(blogRepository.deleteById(1)).thenReturn(expectedResult);
        int result = blogService.deleteBlogById(1);

        assertEquals(expectedResult, result);
    }

    /*ブログ記事を削除する 異常系テスト*/
    @Test
    void testDeleteBlogById_error() {
        int expectedResult = 0;
        when(blogRepository.deleteById(999)).thenReturn(expectedResult);
        int result = blogService.deleteBlogById(999);

        assertEquals(expectedResult, result);
    }

    /*指定したブログ記事のコメント一覧を取得する 正常系テスト*/
    @Test
    void testGetCommentListByBlogId() {
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment(1, 1, "user", "テストコメント1", LocalDateTime.now(), null));
        mockComments.add(new Comment(2, 1, "user", "テストコメント2", LocalDateTime.now(), null));
        mockComments.add(new Comment(3, 1, "user", "テストコメント3", LocalDateTime.now(), null));
        
        long totalItems = 3L;
        int blogId = 1;
        int size = 10;
        int page = 1;  // ← 0から1に変更
        int offset = (page - 1) * size;  // 0

        when(blogRepository.getCommentsOfTheBlog(blogId, size, offset)).thenReturn(mockComments);
        when(blogRepository.getTotalCommentCounts(blogId)).thenReturn(totalItems);
        
        PagedResponse<Comment> result = blogService.getCommentsOfTheBlog(blogId, size, page);
        
        assertNotNull(result);
        assertEquals(3, result.getItems().size());
        assertEquals(1, result.getItems().get(0).getId());
        assertEquals(1, result.getItems().get(0).getBlogId());
        assertEquals("テストコメント1", result.getItems().get(0).getText());
        assertEquals(page, result.getPage());
        assertEquals(size, result.getSize());
        assertEquals(totalItems, result.getTotalItems());
        assertEquals(1, result.getTotalPages());
    }

    /*指定したブログ記事のコメント一覧を取得する 異常系テスト*/
    @Test
    void testGetCommentListByBlogId_error() {
        int blogId = 999;
        int size = 10;
        int page = 1;
        int offset = (page - 1) * size;  // 0
        List<Comment> mockComments = new ArrayList<>();
        long totalItems = 0L;
        
        when(blogRepository.getCommentsOfTheBlog(blogId, size, offset)).thenReturn(mockComments);
        when(blogRepository.getTotalCommentCounts(blogId)).thenReturn(totalItems);
        
        PagedResponse<Comment> result = blogService.getCommentsOfTheBlog(blogId, size, page);
        
        assertNotNull(result);
        assertEquals(0, result.getItems().size());
        assertEquals(page, result.getPage());
        assertEquals(size, result.getSize());
        assertEquals(0, result.getTotalItems());
        assertEquals(0, result.getTotalPages());
    }

    /*指定したブログ記事にコメントを投稿する 正常系テスト*/
    @Test
    void testPostNewComment() {
        int blogId = 1;
        String text = "新規コメント";
        String creator = "user";
        
        // 投稿後のコメントリスト（既存のコメント + 新規コメント）
        List<Comment> mockComments = new ArrayList<>();
        mockComments.add(new Comment(1, blogId, "user", "既存コメント1", LocalDateTime.now(), null));
        mockComments.add(new Comment(2, blogId, "user", "既存コメント2", LocalDateTime.now(), null));
        mockComments.add(new Comment(3, blogId, creator, text, LocalDateTime.now(), null));  // 新規コメント
        
        // モックの戻り値を設定
        // postNewCommentは内部でgetCommentsOfTheBlog(blogId, 10, 0)を呼び出す
        when(blogRepository.postNewComment(blogId, text, creator)).thenReturn(mockComments);
        
        // テスト実行
        List<Comment> result = blogService.postNewComment(blogId, text, creator);
        
        // 検証
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(blogId, result.get(2).getBlogId());
        assertEquals(creator, result.get(2).getCreator());
        assertEquals(text, result.get(2).getText());
        assertNotNull(result.get(2).getCreatedAt());
    }

    /*指定したブログ記事にコメントを投稿する 異常系テスト*/
    @Test
    void testPostNewComment_error() {
        when(blogRepository.postNewComment(999, "テストコメント", "user")).thenReturn(null);
        List<Comment> result = blogService.postNewComment(999, "テストコメント", "user");
        assertNull(result);
    }

    /*指定したコメントを更新する 正常系テスト*/
    @Test
    void testUpdateComment() {
        int expectedResult = 1;
        when(blogRepository.updateComment("更新後のテストコメント", 1)).thenReturn(expectedResult);
        int result = blogService.updateComment("更新後のテストコメント", 1);
        assertEquals(expectedResult, result);
    }

    /*指定したコメントを更新する 異常系テスト*/
    @Test
    void testUpdateComment_error() {
        int expectedResult = 0;
        when(blogRepository.updateComment("テストコメント", 999)).thenReturn(expectedResult);
        int result = blogService.updateComment("テストコメント", 999);
        assertEquals(expectedResult, result);
    }

    /*指定したコメントを削除する 正常系テスト*/
    @Test
    void testDeleteCommentById() {
        int blogId = 1;
        int commentId = 2;
        
        List<Comment> mockCommentsAfterDelete = new ArrayList<>();
        mockCommentsAfterDelete.add(new Comment(1, blogId, "user", "コメント1", LocalDateTime.now(), null));
        mockCommentsAfterDelete.add(new Comment(3, blogId, "user", "コメント3", LocalDateTime.now(), null));
        
        when(blogRepository.deleteCommentById(blogId, commentId)).thenReturn(mockCommentsAfterDelete);

        List<Comment> result = blogService.deleteCommentById(blogId, commentId);
        
        assertNotNull(result);
        assertEquals(2, result.size());  // 削除後は2件
    }

    /*指定したコメントを削除する 異常系テスト*/
    @Test
    void testDeleteCommentById_error() {
        when(blogRepository.deleteCommentById(999, 999)).thenReturn(null);
        List<Comment> result = blogService.deleteCommentById(999, 999);
        assertNull(result);
    }

    /*ブログ記事のランキングを取得する 正常系テスト*/
    @Test
    void testGetRankingOfTheBlog() {
        List<Blog> mockBlogs = new ArrayList<>();
        mockBlogs.add(new Blog(1, 1, "タイトル1", "本文1", LocalDateTime.now(), null, null));
        mockBlogs.add(new Blog(2, 1, "タイトル2", "本文2", LocalDateTime.now(), null, null));
        mockBlogs.add(new Blog(3, 1, "タイトル3", "本文3", LocalDateTime.now(), null, null));
        when(blogRepository.getRankingOfTheBlog()).thenReturn(mockBlogs);
        List<Blog> result = blogService.getRankingOfTheBlog();
        assertNotNull(result);
    }

    /*ブログ記事のランキングを取得する 異常系テスト*/
    @Test
    void testGetRankingOfTheBlog_error() {
        when(blogRepository.getRankingOfTheBlog()).thenReturn(null);
        List<Blog> result = blogService.getRankingOfTheBlog();
        assertNull(result);
    }
}
