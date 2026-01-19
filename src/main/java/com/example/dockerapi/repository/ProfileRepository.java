package com.example.dockerapi.repository;

import java.sql.Timestamp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.dockerapi.model.Profile;

@Repository
public class ProfileRepository {
    
    private final JdbcTemplate jdbcTemplate;

    public ProfileRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /*タレント個別プロフィールを取得（画像URL/その他プロフィール共に）*/
    public Profile getProfile(int id) {
      String sql = """
          SELECT id, image_link, name, age, `from`, height, hobby, created_at, updated_at
          FROM talents
          WHERE id = ? AND deleted_at IS NULL
          """;
      
      try {
          return jdbcTemplate.queryForObject(sql, profileRowMapper(), id);
      } catch (org.springframework.dao.EmptyResultDataAccessException e) {
          return null;
      }
    }

    /*RowMapper*/
    private RowMapper<Profile> profileRowMapper() {
        return (rs, rowNum) -> {
            int id = rs.getInt("id");
            String imageLink = rs.getString("image_link");
            String name = rs.getString("name");
            int age = rs.getInt("age");
            String from = rs.getString("from");
            int height = rs.getInt("height");
            String hobby = rs.getString("hobby");
            Timestamp createdAt = rs.getTimestamp("created_at");
            Timestamp updatedAt = rs.getTimestamp("updated_at");
            
            return new Profile(
                id,
                imageLink,
                name,
                age,
                from,
                height,
                hobby,
                createdAt != null ? createdAt.toLocalDateTime() : null,
                updatedAt != null ? updatedAt.toLocalDateTime() : null        
            );
        };
    }

    /*個別にプロフィール情報を編集する(画像以外)*/
    public int updateProfile(String image_link, String name, int age, String from, int height, String hobby, int id) {
      String sql = """
          UPDATE talents
          SET image_link = ?, name = ?, age = ?, `from` = ?, height = ?, hobby = ?, updated_at = NOW()
          WHERE id = ? AND deleted_at IS NULL
          """;
      return jdbcTemplate.update(sql, image_link, name, age, from, height, hobby, id);
    }

    /*プロフィール画像を更新する*/
    public int updateImageLink(String url, int id) {
        String sql = """
            UPDATE talents
            SET image_link = ?, updated_at = NOW()
            WHERE id = ? AND deleted_at IS NULL
            """;
        return jdbcTemplate.update(sql, url, id);
    }
}