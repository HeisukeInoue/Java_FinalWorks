package com.example.dockerapi.repository;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.dockerapi.model.Blog;

@Repository
public class SearchRepository {
    
    private final JdbcTemplate jdbcTemplate;

    public SearchRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Blog> searchBlogs(String query) {

        String[] words = query.split("\\s+"); //スペースで検索文字列(例:A B C)を分割する ("\\s+"はスペースの正規表現)
        StringBuilder sql = new StringBuilder("SELECT * FROM blogs WHERE ");
    
        for (int i = 0; i < words.length; i++) { //分割した検索ワードの数だけループ/OR検索を行う
            sql.append("title LIKE ?");
            if (i < words.length - 1) {
                sql.append(" OR ");
            }
        }
    
        RowMapper<Blog> mapper = (rs, rowNum) -> new Blog(
            rs.getInt("id"),
            rs.getInt("talent_id"),
            rs.getString("title"),
            rs.getString("text"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
            rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null
        );
    
        Object[] params = new Object[words.length]; 

        for (int i = 0; i < words.length; i++) {
            params[i] = "%" + words[i] + "%";
        } //前後の任意の文字列を許容するように変換する
    
        return jdbcTemplate.query(sql.toString(), mapper, params);
    }
}