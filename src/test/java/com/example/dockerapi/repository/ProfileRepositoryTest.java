package com.example.dockerapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import com.example.dockerapi.model.Profile;

@JdbcTest
@Import(ProfileRepository.class)
@Sql(scripts = "/testdata/profile_test_data.sql")
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;
    /*タレント個別プロフィールを取得（画像URL/その他プロフィール共に） 正常系テスト*/
    @Test
    void testGetProfile() {
        Profile profile = profileRepository.getProfile(1);
        assertNotNull(profile);
        assertEquals(1, profile.getId());
        assertEquals("テストタレント1", profile.getName());
        assertEquals("https://example.com/image.jpg", profile.getImageLink());
        assertEquals(20, profile.getAge());
        assertEquals("東京", profile.getFrom());
        assertEquals(170, profile.getHeight());
        assertEquals("テストタレント1の趣味", profile.getHobby());
    }

    /*タレント個別プロフィールを取得（画像URL/その他プロフィール共に） 異常系テスト*/
    @Test
    void testGetProfile_error() {
        Profile profile = profileRepository.getProfile(999);
        assertNull(profile);
    }

    /*個別にプロフィール情報を更新する(画像以外) 正常系テスト*/
    @Test
    void testUpdateProfile() {
        int result = profileRepository.updateProfile("https://example.com/image.jpg", "更新後のタレント名", 20, "大阪", 200, "更新後のテストタレント1の趣味", 1);
        assertEquals(1, result);
        Profile profile = profileRepository.getProfile(1);
        assertNotNull(profile);
        assertEquals(1, profile.getId());
        assertEquals("更新後のタレント名", profile.getName());
        assertEquals("https://example.com/image.jpg", profile.getImageLink());
        assertEquals(20, profile.getAge());
        assertEquals("大阪", profile.getFrom());
        assertEquals(200, profile.getHeight());
        assertEquals("更新後のテストタレント1の趣味", profile.getHobby());
    }

    /*個別にプロフィール情報を更新する(画像以外) 異常系テスト*/
    @Test
    void testUpdateProfile_error() {
        int result = profileRepository.updateProfile("https://example.com/image.jpg", "更新後のタレント名", 20, "大阪", 200, "更新後のテストタレント1の趣味", 999);
        assertEquals(0, result);
    }

    /*プロフィール画像を更新する 正常系テスト*/
    @Test
    void testUpdateImageLink() {
        int result = profileRepository.updateImageLink("https://example.com/update_image.jpg", 1);
        assertEquals(1, result);
        Profile profile = profileRepository.getProfile(1);
        assertNotNull(profile);
        assertEquals(1, profile.getId());
        assertEquals("https://example.com/update_image.jpg", profile.getImageLink());
    }

    /*プロフィール画像を更新する 異常系テスト*/
    @Test
    void testUpdateImageLink_error() {
        int result = profileRepository.updateImageLink("https://example.com/update_image.jpg", 999);
        assertEquals(0, result);
    }
}
