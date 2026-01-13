package com.example.dockerapi.repository; //このクラスが属する**パッケージ（フォルダ階層）**の宣言。
// import java.sql.PreparedStatement;
// import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.example.dockerapi.model.Appearance; 

@Repository
public class AppearanceRepository {
    private final JdbcTemplate jdbcTemplate;

    public AppearanceRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /*タレント出演情報の一覧を取得する*/
    public List<Appearance> getAllAppearance() {
        String sql = """
            SELECT id, talent_id, date, title, created_at, updated_at
            FROM appearances
            WHERE deleted_at IS NULL
            ORDER BY created_at DESC
            """;
  
        RowMapper<Appearance> mapper = (rs, rowNum) -> new Appearance(
            rs.getInt("id"),
            rs.getInt("talent_id"),
            rs.getDate("date"),
            rs.getString("title"),
            rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
            rs.getTimestamp("updated_at") != null ? rs.getTimestamp("updated_at").toLocalDateTime() : null
        );
        
        return jdbcTemplate.query(sql, mapper);    
      }

}
