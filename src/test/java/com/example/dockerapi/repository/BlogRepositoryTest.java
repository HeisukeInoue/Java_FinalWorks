package com.example.dockerapi.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.jdbc.Sql;
import com.example.dockerapi.model.Blog;
import com.example.dockerapi.model.Comment;

@JdbcTest
@Import(BlogRepository.class)
@Sql(scripts = "/testdata/blogs_test_data.sql")
class BlogRepositoryTest {
    @Autowired
    private BlogRepository blogRepository;

    /*個別にブログ記事を取得・閲覧する 正常系テスト*/
    @Test
    void testFindById() {
        Blog blog = blogRepository.findById(1);

        assertNotNull(blog);
        assertEquals(1, blog.getId());
        assertEquals("テストタイトル1", blog.getTitle());
        assertEquals("テスト本文1", blog.getText());
    }

    /*個別にブログ記事を取得・閲覧する 異常系テスト*/
    @Test
    void testFindById_error() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            blogRepository.findById(999); // 存在しないID
        });
    }

    /*ブログ記事リストを取得する 正常系テスト*/
    @Test
    void testGetBlogsByCurrentPage() {
        List<Blog> blogs = blogRepository.getBlogsByCurrentPage(10, 0);

        assertNotNull(blogs);
        assertEquals(10, blogs.size());
    }

    /*ブログ記事リストを取得する 異常系テスト*/
    @Test
    void testGetBlogsByCurrentPage_error() {
        // pageSizeが0の場合、空のリストが返ることを確認
        List<Blog> blogs = blogRepository.getBlogsByCurrentPage(0, 0);
        assertNotNull(blogs);
        assertEquals(0, blogs.size());
    }

    /*ブログ総件数を取得する 正常系テスト*/
    @Test
    void testGetTotalBlogCounts() {
        Long totalBlogCounts = blogRepository.getTotalBlogCounts();
        assertNotNull(totalBlogCounts);
        assertEquals(11, totalBlogCounts.intValue());
    }

    /*ブログ記事を投稿する 正常系テスト*/
    @Test
    void testPostNewBlogs() {
        Blog blog = blogRepository.postNewBlogs("テストタイトル1", "テスト本文1");
        assertNotNull(blog);
        assertEquals(12, blog.getId());
        assertEquals("テストタイトル1", blog.getTitle());
        assertEquals("テスト本文1", blog.getText());
    }
    
    /*ブログ記事を投稿する 異常系テスト*/
    @Test
    void testPostNewBlogs_error() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            blogRepository.postNewBlogs(null, null);
        });
    }

    /*ブログ記事を更新する 正常系テスト*/
    @Test
    void testUpdateBlogs() {
        int result = blogRepository.updateBlogs("テストタイトル2", "テスト本文2", 1);
        assertEquals(1, result);
    }

    /*ブログ記事を更新する 異常系テスト*/
    @Test
    void testUpdateBlogs_error() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            blogRepository.updateBlogs(null, null, 1);
        });
    }

    /*ブログ記事を削除する 正常系テスト*/
    @Test
    void testDeleteById() {
        int result = blogRepository.deleteById(1);
        assertEquals(1, result);
    }

    /*ブログ記事を削除する 異常系テスト*/
    @Test
    void testDeleteById_error() {
        int result = blogRepository.deleteById(999);
        assertEquals(0, result);
    }

    /*指定したブログ記事のコメント一覧を取得する 正常系テスト*/
    @Test
    void testGetCommentsOfTheBlog() {
        List<Comment> comments = blogRepository.getCommentsOfTheBlog(1, 10, 10);
        assertNotNull(comments);
        assertEquals(4, comments.size()); //レコード総件数14件から、11件目以降の4件を取得
    }

    /*指定したブログ記事のコメント一覧を取得する 異常系テスト1*/
    @Test
    void testGetCommentsOfTheBlog_error() {
        List<Comment> comments = blogRepository.getCommentsOfTheBlog(999, 10, 0);
        assertNotNull(comments);
        assertEquals(0, comments.size()); //存在しないブログ記事IDの場合、空のリストが返る
    }

    /*指定したブログ記事のコメント一覧を取得する 異常系テスト2*/
    @Test
    void testGetCommentsOfTheBlog_error2() {
        List<Comment> comments = blogRepository.getCommentsOfTheBlog(1, 10, 999);
        assertNotNull(comments);
        assertEquals(0, comments.size()); //offsetが999の場合、空のリストが返る
    }

    /*指定したブログ記事のコメント総件数を取得する 正常系テスト*/
    @Test
    void testGetTotalCommentCounts() {
        Long totalCommentCounts = blogRepository.getTotalCommentCounts(1);
        assertNotNull(totalCommentCounts);
        assertEquals(14, totalCommentCounts.intValue());
    }

    /*指定したブログ記事のコメント総件数を取得する 異常系テスト*/
    @Test
    void testGetTotalCommentCounts_error() {
        Long totalCommentCounts = blogRepository.getTotalCommentCounts(999);
        assertNotNull(totalCommentCounts);
        assertEquals(0, totalCommentCounts.intValue());
    }

    /*指定したブログ記事にコメントを投稿する 正常系テスト*/
    @Test
    void testPostNewComment() {
        List<Comment> comments = blogRepository.postNewComment(1, "テストコメント1", "テストユーザー1");
        assertNotNull(comments);
        assertEquals(10, comments.size());
    }

    /*指定したブログ記事にコメントを投稿する 異常系テスト*/
    @Test
    void testPostNewComment_error() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            blogRepository.postNewComment(999, "テストコメント1", "テストユーザー1");
        });
    }
    
    /*指定したコメントを更新する 正常系テスト*/
    @Test
    void testUpdateComment() {
        int result = blogRepository.updateComment("テストコメント2", 1);
        assertEquals(1, result);
    }

    /*指定したコメントを更新する 異常系テスト*/
    @Test
    void testUpdateComment_error() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            blogRepository.updateComment(null, 1);
        });
    }

    /*指定したコメントを更新する 異常系テスト2*/
    @Test
    void testUpdateComment_error2() {
        int result = blogRepository.updateComment("テストコメント2", 999);
        assertEquals(0, result);
    }

    /*指定したコメントを削除する 正常系テスト*/
    @Test
    void testDeleteCommentById() {
        List<Comment> comments = blogRepository.deleteCommentById(1, 1);
        assertNotNull(comments);
        assertEquals(10, comments.size());
    }

    /*指定したコメントを削除する 異常系テスト*/
    @Test
    void testDeleteCommentById_error() {
        List<Comment> comments = blogRepository.deleteCommentById(999, 1);
        assertNotNull(comments);
        assertEquals(0, comments.size());
    }

    /*指定したコメントを削除する 異常系テスト2*/
    @Test
    void testDeleteCommentById_error2() {
        List<Comment> comments = blogRepository.deleteCommentById(1, 999);
        assertNotNull(comments);
        assertEquals(10, comments.size());
    }

    /*ブログ記事のランキングを取得する 正常系テスト*/
    @Test
    void testGetRankingOfTheBlog() {
        List<Blog> blogs = blogRepository.getRankingOfTheBlog();
        assertNotNull(blogs);
        assertEquals(5, blogs.size());
        assertEquals(1, blogs.get(0).getId());
        assertEquals(3, blogs.get(1).getId());
        assertEquals(9, blogs.get(2).getId());
        assertEquals(8, blogs.get(3).getId());
        assertEquals(10, blogs.get(4).getId()); 
    }
}