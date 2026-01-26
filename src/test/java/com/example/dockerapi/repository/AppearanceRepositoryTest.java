package com.example.dockerapi.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import com.example.dockerapi.model.Appearance;
import com.example.dockerapi.model.AppearanceDetail;

@JdbcTest
@Import(AppearanceRepository.class)
@Sql(scripts = {
    "/testdata/profile_test_data.sql",
    "/testdata/appearances_test_data.sql"  
})
class AppearanceRepositoryTest {

    @Autowired
    private AppearanceRepository appearanceRepository;

    /*タレント出演情報の一覧を取得する 正常系テスト*/
    @Test
    void testGetAllAppearance() {
        List<Appearance> appearances = appearanceRepository.getAllAppearance();
        assertNotNull(appearances);
        assertEquals(3, appearances.size());
    }

    /*タレント出演情報の詳細を取得する 正常系テスト*/
    @Test
    void testGetAppearanceDetail() {
        AppearanceDetail appearanceDetail = appearanceRepository.getAppearanceDetail(1);
        assertNotNull(appearanceDetail);
        assertEquals(1, appearanceDetail.getId());
        assertEquals(1, appearanceDetail.getTalentId());
        assertEquals("2024-01-01", appearanceDetail.getDate().toString());
        assertEquals("テストタイトル1", appearanceDetail.getTitle());
        assertEquals("テスト本文1", appearanceDetail.getText());
    }

    /*タレント出演情報の詳細を取得する 異常系テスト*/
    @Test
    void testGetAppearanceDetail_error() {
        AppearanceDetail appearanceDetail = appearanceRepository.getAppearanceDetail(100);
        assertNull(appearanceDetail);
    }

    /*タレント出演情報を投稿する 正常系テスト*/
    @Test
    void testPostNewAppearance() {
        appearanceRepository.postNewAppearance(Date.valueOf("2025-01-01"), "テストタイトル1", "テスト本文1");
        AppearanceDetail appearanceDetail = appearanceRepository.getAppearanceDetail(4); //INSERT文に3レコード=投稿後のIDを取得するため、4を指定
        assertNotNull(appearanceDetail);
        assertEquals(4, appearanceDetail.getId());
        assertEquals(1, appearanceDetail.getTalentId());
        assertEquals("2025-01-01", appearanceDetail.getDate().toString());
        assertEquals("テストタイトル1", appearanceDetail.getTitle());
        assertEquals("テスト本文1", appearanceDetail.getText());
    }

    /*タレント出演情報を投稿する 異常系テスト*/
    @Test
    void testPostNewAppearance_error() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            appearanceRepository.postNewAppearance(null, null, null);
        });
    }

    /*タレント出演情報を編集する 正常系テスト*/
    @Test
    void testUpdateAppearance() {
        appearanceRepository.updateAppearance(Date.valueOf("2025-01-01"), "更新後テストタイトル2", "更新後テスト本文2", 1);
        AppearanceDetail appearanceDetail = appearanceRepository.getAppearanceDetail(1);
        assertNotNull(appearanceDetail);
        assertEquals(1, appearanceDetail.getId());
        assertEquals(1, appearanceDetail.getTalentId());
        assertEquals("2025-01-01", appearanceDetail.getDate().toString());
        assertEquals("更新後テストタイトル2", appearanceDetail.getTitle());
        assertEquals("更新後テスト本文2", appearanceDetail.getText());
    }

    /*タレント出演情報を編集する 異常系テスト1*/
    @Test
    void testUpdateAppearance_error() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            appearanceRepository.updateAppearance(null, null, null, 1);
        });
    }

    /*タレント出演情報を編集する 異常系テスト2*/
    @Test
    void testUpdateAppearance_error2() {
        int result = appearanceRepository.updateAppearance(Date.valueOf("2025-01-01"), "更新後テストタイトル2", "更新後テスト本文2", 999);
        /*更新された行数が0であることを確認*/
        assertEquals(0, result);
    }

    /*タレント出演情報を削除する 正常系テスト*/
    @Test
    void testDeleteAppearance() {
        int result = appearanceRepository.deleteAppearance(1);
        assertEquals(1, result);
        /*削除されたレコードが存在しないことを確認*/
        AppearanceDetail appearanceDetail = appearanceRepository.getAppearanceDetail(1);
        assertNull(appearanceDetail);
    }

    /*タレント出演情報を削除する 異常系テスト*/
    @Test
    void testDeleteAppearance_error() {
        int result = appearanceRepository.deleteAppearance(999);
        assertEquals(0, result);
    }
}