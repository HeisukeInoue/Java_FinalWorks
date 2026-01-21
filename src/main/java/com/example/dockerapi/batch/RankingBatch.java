package com.example.dockerapi.batch;

import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class RankingBatch {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public Job updateBlogRanking(Step rankingStep) {
        return new JobBuilder("updateBlogRanking", jobRepository)
                .start(rankingStep)
                .build();
    }

    @Bean
    public Step rankingStep(ItemReader<BlogRankingData> reader, ItemWriter<BlogRankingData> writer) {
        return new StepBuilder("rankingStep", jobRepository)
                .<BlogRankingData, BlogRankingData>chunk(5, transactionManager)
                .reader(reader)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    //commentsテーブルのレコード数をblog_id別に集計（上位5件）
    @Bean
    public ItemReader<BlogRankingData> rankingReader() {
        String sql = """
            SELECT blog_id, COUNT(*) AS comment_count
            FROM comments
            WHERE deleted_at IS NULL
            GROUP BY blog_id
            ORDER BY COUNT(*) DESC
            LIMIT 5
            """;
        
        List<BlogRankingData> blogData = jdbcTemplate.query(sql, (rs, rowNum) -> {
            BlogRankingData data = new BlogRankingData();
            data.setBlogId(rs.getInt("blog_id"));  
            data.setCommentCount(rs.getInt("comment_count"));
            return data;
        });
        
        return new ListItemReader<>(blogData);
    }

    //Readerから受け取ったデータを使ってrankingテーブルを更新
    @Bean
    public ItemWriter<BlogRankingData> rankingWriter() {
        return items -> {
            String updateSql = """
                UPDATE ranking 
                SET blog_id = ?, comment_count = ?, updated_at = NOW()
                WHERE id = ?
                """;
            
            int rank = 1;  
            for (BlogRankingData item : items) {
                jdbcTemplate.update(updateSql, 
                    item.getBlogId(),      
                    item.getCommentCount(),
                    rank                   
                );
                rank++;
            }
        };
    }

    public static class BlogRankingData {
        private int blogId;
        private int commentCount;

        public int getBlogId() { return blogId; }
        public void setBlogId(int blogId) { this.blogId = blogId; }
        
        public int getCommentCount() { return commentCount; }
        public void setCommentCount(int commentCount) { this.commentCount = commentCount; }
    }
}