package com.example.dockerapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Date;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.dockerapi.model.AppearanceDetail;
import com.example.dockerapi.repository.AppearanceRepository;


@ExtendWith(MockitoExtension.class)
class AppearanceServiceTest {
    
    @Mock
    private AppearanceRepository appearanceRepository;

    @InjectMocks
    private AppearanceService appearanceService;

    /*個別にプロフィールを取得する 正常系テスト*/
    @Test
    void testGetAppearanceDetail() {
        AppearanceDetail dummyAppearanceDetail = new AppearanceDetail(
            1, 
            1, 
            Date.valueOf("2024-01-01"), 
            "テストタイトル", 
            "テスト本文", 
            LocalDateTime.now(), 
            null
        );
        when(appearanceRepository.getAppearanceDetail(1)).thenReturn(dummyAppearanceDetail);
        AppearanceDetail result = appearanceService.getAppearanceDetail(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(1, result.getTalentId());
        assertEquals("2024-01-01", result.getDate().toString());
        assertEquals("テストタイトル", result.getTitle());
        assertEquals("テスト本文", result.getText());
    }

    /*個別にプロフィールを取得する 異常系テスト*/
    @Test
    void testGetAppearanceDetail_error() {
        when(appearanceRepository.getAppearanceDetail(999)).thenReturn(null);
        AppearanceDetail result = appearanceService.getAppearanceDetail(999);
        assertNull(result);
    }

    /*タレント出演情報を投稿する 正常系テスト*/
    @Test
    void testPostNewAppearance() {
        Date date = Date.valueOf("2024-01-01");
        String title = "テストタイトル";
        String text = "テスト本文";

        doNothing().when(appearanceRepository).postNewAppearance(date, title, text);
        appearanceService.postNewAppearance(date, title, text);
        verify(appearanceRepository).postNewAppearance(date, title, text);
    }

    /*タレント出演情報を投稿する 異常系テスト*/
    @Test
    void testPostNewAppearance_error() {
        Date date = Date.valueOf("2024-01-01");
        String title = "テストタイトル";
        String text = null;
        doThrow(new IllegalArgumentException("出演情報の登録に失敗しました")).when(appearanceRepository).postNewAppearance(date, title, text);
        assertThrows(IllegalArgumentException.class, () -> appearanceService.postNewAppearance(date, title, text));
    }

    /*タレント出演情報を更新する 正常系テスト*/
    @Test
    void testUpdateAppearance() {
        Date date = Date.valueOf("2026-01-27");
        String title = "更新後テストタイトル";
        String text = "更新後テスト本文";
        int expected = 1;
        when(appearanceRepository.updateAppearance(date, title, text, 1)).thenReturn(expected);
        int result = appearanceService.updateAppearance(date, title, text, 1);
        assertEquals(expected, result);
        verify(appearanceRepository).updateAppearance(date, title, text, 1);
    }

    /*タレント出演情報を更新する 異常系テスト*/
    @Test
    void testUpdateAppearance_error() {
        Date date = Date.valueOf("2024-01-01");
        String title = "更新後テストタイトル";
        String text = null;
        doThrow(new IllegalArgumentException("出演情報の更新に失敗しました")).when(appearanceRepository).updateAppearance(date, title, text, 1);
        assertThrows(IllegalArgumentException.class, () -> appearanceService.updateAppearance(date, title, text, 1));
    }

    /*タレント出演情報を削除する 正常系テスト*/
    @Test
    void testDeleteAppearance() {
        int expected = 1;
        when(appearanceRepository.deleteAppearance(1)).thenReturn(expected);
        int result = appearanceService.deleteAppearance(1);
        assertEquals(expected, result);
        verify(appearanceRepository).deleteAppearance(1);
    }

    /*タレント出演情報を削除する 異常系テスト*/
    @Test
    void testDeleteAppearance_error() {
        doThrow(new IllegalArgumentException("出演情報の削除に失敗しました")).when(appearanceRepository).deleteAppearance(1);
        assertThrows(IllegalArgumentException.class, () -> appearanceService.deleteAppearance(1));
    }
}
