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
        String sql = "SELECT * FROM blogs WHERE title LIKE ?";
        String pattern = "%" + query + "%";
        RowMapper<Blog> mapper = (rs, rowNum) -> new Blog(
            rs.getInt("id"),
            rs.getInt("talent_id"),
            rs.getString("title"),
            rs.getString("text"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
            rs.getTimestamp("deleted_at") != null ? rs.getTimestamp("deleted_at").toLocalDateTime() : null
        );
        return jdbcTemplate.query(sql, mapper, pattern);
    }
}