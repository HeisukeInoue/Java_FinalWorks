package com.example.dockerapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.dockerapi.model.Profile;
import com.example.dockerapi.repository.ProfileRepository;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {
    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileService profileService;

    /*タレント個別プロフィールを取得（画像URL/その他プロフィール共に） 正常系テスト*/
    @Test
    void testGetProfile() {
        when(profileRepository.getProfile(1)).thenReturn(new Profile(1, "https://example.com/image.jpg", "テストタレント1", 20, "大阪", 200, "テストタレント1の趣味", LocalDateTime.now(), LocalDateTime.now()));
        Profile profile = profileService.getProfile(1);
        assertEquals(1, profile.getId());
        assertEquals("https://example.com/image.jpg", profile.getImageLink());
    }

    /*タレント個別プロフィールを取得（画像URL/その他プロフィール共に） 異常系テスト*/
    @Test
    void testGetProfile_error() {
        when(profileRepository.getProfile(999)).thenReturn(null);
        Profile profile = profileService.getProfile(999);
        assertNull(profile);
    }

    /*個別にプロフィール情報を編集する(画像以外) 正常系テスト*/
    @Test
    void testUpdateProfile() {
        when(profileRepository.updateProfile("https://example.com/image.jpg", "更新後のタレント名", 20, "大阪", 200, "更新後のテストタレント1の趣味", 1)).thenReturn(1);
        int result = profileService.updateProfile("https://example.com/image.jpg", "更新後のタレント名", 20, "大阪", 200, "更新後のテストタレント1の趣味", 1);
        assertEquals(1, result);
    }

    /*個別にプロフィール情報を編集する(画像以外) 異常系テスト*/
    @Test
    void testUpdateProfile_error() {
        when(profileRepository.updateProfile("https://example.com/image.jpg", "更新後のタレント名", 20, "大阪", 200, "更新後のテストタレント1の趣味", 999)).thenReturn(0);
        int result = profileService.updateProfile("https://example.com/image.jpg", "更新後のタレント名", 20, "大阪", 200, "更新後のテストタレント1の趣味", 999);
        assertEquals(0, result);
    }

    /*プロフィール画像を更新する 正常系テスト*/
    @Test
    void testUpdateImageLink() {
        when(profileRepository.updateImageLink("https://example.com/image.jpg", 1)).thenReturn(1);
        int result = profileService.updateImageLink("https://example.com/image.jpg", 1);
        assertEquals(1, result);
    }

    /*プロフィール画像を更新する 異常系テスト*/
    @Test
    void testUpdateImageLink_error() {
    }   
}
