package com.example.dockerapi.service;

import com.example.dockerapi.model.User;
import com.example.dockerapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    /* getUserById() に対するテスト - コントローラー層の@GetMapping("/{user_id}")と対応 */
    @Test
    void testGetUserById_Success() {
        // テストデータの準備（コントローラー層と同じデータ）
        int userId = 1;
        User expectedUser = new User(1, "山田太郎", "yamada@example.com");

        // UserRepositoryのモック設定
        when(userRepository.findById(userId)).thenReturn(expectedUser);

        // テスト実行
        User actualUser = userService.getUserById(userId);

        // 検証（コントローラー層と同じ検証内容）
        assertNotNull(actualUser);
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());

        // UserRepositoryが正しい引数で呼ばれたことを検証
        verify(userRepository).findById(eq(userId));
    }

    /* getUserById() に対するテスト - コントローラー層のtestGetUserById_NotFound()と対応 */
    @Test
    void testGetUserById_NotFound() {
        // テストデータの準備（コントローラー層と同じデータ）
        int userId = 999;

        // UserRepositoryのモック設定：ユーザーが見つからない場合
        when(userRepository.findById(userId))
                .thenThrow(new EmptyResultDataAccessException(1));

        // テスト実行と検証（コントローラー層では500エラーになるが、サービス層では例外がスローされる）
        assertThrows(EmptyResultDataAccessException.class, () -> {
            userService.getUserById(userId);
        });

        // UserRepositoryが正しい引数で呼ばれたことを検証
        verify(userRepository).findById(eq(userId));
    }

    /* getUserById() に対するテスト - コントローラー層のtestGetUserById_WithDifferentUserId()と対応 */
    @Test
    void testGetUserById_WithDifferentUserId() {
        // 異なるユーザーIDでのテスト（コントローラー層と同じデータ）
        int userId = 2;
        User expectedUser = new User(2, "佐藤花子", "sato@example.com");

        // UserRepositoryのモック設定
        when(userRepository.findById(userId)).thenReturn(expectedUser);

        // テスト実行
        User actualUser = userService.getUserById(userId);

        // 検証（コントローラー層と同じ検証内容）
        assertNotNull(actualUser);
        assertEquals(2, actualUser.getId());
        assertEquals("佐藤花子", actualUser.getName());
        assertEquals("sato@example.com", actualUser.getEmail());

        // UserRepositoryが正しい引数で呼ばれたことを検証
        verify(userRepository).findById(eq(userId));
    }
}