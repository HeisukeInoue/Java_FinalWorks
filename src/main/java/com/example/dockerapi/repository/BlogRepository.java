package com.example.dockerapi.repository; //このクラスが属する**パッケージ（フォルダ階層）**の宣言。

import java.util.List;
/*import org.springframework.beans.factory.annotation.Autowired;*/ //フィールド注入は非推奨
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository; //Springに「このクラスはRepositoryです」と認識させ、DI対象にするアノテーション
import com.example.dockerapi.model.Blog;

@Repository
public class BlogRepository {

    private final JdbcTemplate jdbcTemplate;

    public BlogRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /*リクエストのあったidで個別にブログ記事を取得する*/
    public Blog findById(int id) {
        String sql = """
            SELECT id, talent_id, title, text, created_at, updated_at, deleted_at
            FROM blogs
            WHERE id = ? AND deleted_at IS NULL;
            """;
        
          RowMapper<Blog> mapper = (rs, rowNum) -> new Blog(
            rs.getInt("id"),
            rs.getInt("talent_id"),
            rs.getString("title"),
            rs.getString("text"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
            rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null
        );
    
        return jdbcTemplate.queryForObject(sql, mapper, id);
    }

    /*指定のあったページindexをもとにブログ記事リストを取得する*/
    public List<Blog> getBlogsByCurrentPage(int pageSize, int offSet) {
      String sql = """
          SELECT id, talent_id, title, text, created_at, updated_at, deleted_at
          FROM blogs
          WHERE deleted_at IS NULL
          ORDER BY created_at DESC
          LIMIT ?
          OFFSET ?
          """;

      RowMapper<Blog> mapper = (rs, rowNum) -> new Blog(
          rs.getInt("id"),
          rs.getInt("talent_id"),
          rs.getString("title"),
          rs.getString("text"),
          rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
          rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
          rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null
      );
      
      return jdbcTemplate.query(sql, mapper, pageSize, offSet);    
    }

    /*ブログ総件数を取得する*/
    public Long getTotalBlogCounts() {
      String sql = """
          SELECT COUNT(*)
          FROM blogs
          WHERE deleted_at IS NULL
          """;
      return jdbcTemplate.queryForObject(sql, Long.class);    
    }

    /*ブログ記事を投稿する
    public Blog postBlog(int talent_id, String title, String text) {
      String sql = """
          INSERT
          INTO blogs(talent_id, title, text, created_at, updated_at, deleted_at)
          VALUES (?, ?, ?, NOW(), NULL, NULL)
          """;
    
      
    }*/

    /*個別にブログ記事を更新する */
    public int updateBlogs(String blogtitle, String blogtext, int blogid) {
      String sql = """
          UPDATE blogs
          SET title = ?, text = ?, updated_at = NOW()
          WHERE id = ?
          """;
      return jdbcTemplate.update(sql, blogtitle, blogtext, blogid);
    }

    /*個別にブログ記事を削除する*/
    public int deleteById(int id) {
      String sql = """
          UPDATE blogs
          SET deleted_at = NOW()
          WHERE id = ?
          """;
      
      return jdbcTemplate.update(sql, id);
    }
}