package com.example.dockerapi.repository; //このクラスが属する**パッケージ（フォルダ階層）**の宣言。
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.example.dockerapi.model.appearance;
import com.example.dockerapi.model.appearanceDetail; 

@Repository
public class appearanceRepository {
    private final JdbcTemplate jdbcTemplate;

    public appearanceRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /*タレント出演情報の一覧を取得する*/
    public List<appearance> getAllappearance() {
        String sql = """
            SELECT id, talent_id, date, title, created_at, updated_at
            FROM appearances
            WHERE deleted_at IS NULL
            ORDER BY created_at DESC
            """;
  
        RowMapper<appearance> mapper = (rs, rowNum) -> new appearance(
            rs.getInt("id"),
            rs.getInt("talent_id"),
            rs.getDate("date"),
            rs.getString("title"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
        
        return jdbcTemplate.query(sql, mapper);    
      }

    /*出演情報の詳細を取得する*/
    public appearanceDetail getappearanceDetail(int id) {
        String sql = """
            SELECT id, talent_id, date, title, text, created_at, updated_at
            FROM appearances
            WHERE deleted_at IS NULL AND id = ?
            """;
  
        RowMapper<appearanceDetail> mapper = (rs, rowNum) -> new appearanceDetail(
            rs.getInt("id"),
            rs.getInt("talent_id"),
            rs.getDate("date"),
            rs.getString("title"),
            rs.getString("text"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
        
        return jdbcTemplate.queryForObject(sql, mapper, id);    
      }

    /*出演情報を投稿する*/
    public void postNewappearance(Date date, String title, String text) {

        String sql = """
            INSERT INTO appearances(talent_id, date, title, text, created_at, updated_at)
            VALUES (1, ?, ?, ?, NOW(), NULL)
            """;
    
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, date);
            ps.setString(2, title);
            ps.setString(3, text);
            return ps;
        }, keyHolder);
    }

    /*出演情報を編集する*/
    public int updateappearance(Date date, String title, String text, int id) {
        String sql = """
            UPDATE appearances
            SET date = ?, title = ?, text = ?, updated_at = NOW()
            WHERE id = ?
            """;
        return jdbcTemplate.update(sql, date, title, text, id);
    }

    /*出演情報を削除する*/
    public int deleteappearance(int id) {
        String sql = """
            UPDATE appearances
            SET deleted_at = NOW()
            WHERE id = ?
            """;
        
        return jdbcTemplate.update(sql, id);
    }
}
