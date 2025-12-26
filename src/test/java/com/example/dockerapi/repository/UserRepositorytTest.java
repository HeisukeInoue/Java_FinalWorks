package com.example.dockerapi.repository;

import com.example.dockerapi.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositorytTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserRepository userRepository;

    /* findById() に対するテスト - コントローラー層の@GetMapping("/{user_id}")と対応 */
    @Test
    void testFindById_Success() {
        // テストデータの準備（コントローラー層と同じデータ）
        int userId = 1;
        User expectedUser = new User(1, "山田太郎", "yamada@example.com");

        // JdbcTemplateのモック設定
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                eq(userId)
        )).thenReturn(expectedUser);

        // テスト実行
        User actualUser = userRepository.findById(userId);

        // 検証（コントローラー層と同じ検証内容）
        assertNotNull(actualUser);
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());

        // JdbcTemplateが正しい引数で呼ばれたことを検証
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<RowMapper<User>> rowMapperCaptor = ArgumentCaptor.forClass(RowMapper.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        verify(jdbcTemplate).queryForObject(
                sqlCaptor.capture(),
                rowMapperCaptor.capture(),
                idCaptor.capture()
        );

        // SQLが正しいことを検証
        String capturedSql = sqlCaptor.getValue();
        assertNotNull(capturedSql);
        assertTrue(capturedSql.contains("SELECT"));
        assertTrue(capturedSql.contains("FROM users"));
        assertTrue(capturedSql.contains("WHERE id = ?"));
        assertEquals(userId, idCaptor.getValue());
    }

    /* findById() に対するテスト - コントローラー層のtestGetUserById_NotFound()と対応 */
    @Test
    void testFindById_NotFound() {
        // テストデータの準備（コントローラー層と同じデータ）
        int userId = 999;

        // JdbcTemplateのモック設定：ユーザーが見つからない場合
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                eq(userId)
        )).thenThrow(new EmptyResultDataAccessException(1));

        // テスト実行と検証（コントローラー層では500エラーになるが、リポジトリ層では例外がスローされる）
        assertThrows(EmptyResultDataAccessException.class, () -> {
            userRepository.findById(userId);
        });
    }

    /* findById() に対するテスト - コントローラー層のtestGetUserById_WithDifferentUserId()と対応 */
    @Test
    void testFindById_WithDifferentUserId() {
        // 異なるユーザーIDでのテスト（コントローラー層と同じデータ）
        int userId = 2;
        User expectedUser = new User(2, "佐藤花子", "sato@example.com");

        // JdbcTemplateのモック設定
        when(jdbcTemplate.queryForObject(
                anyString(),
                any(RowMapper.class),
                eq(userId)
        )).thenReturn(expectedUser);

        // テスト実行
        User actualUser = userRepository.findById(userId);

        // 検証（コントローラー層と同じ検証内容）
        assertNotNull(actualUser);
        assertEquals(2, actualUser.getId());
        assertEquals("佐藤花子", actualUser.getName());
        assertEquals("sato@example.com", actualUser.getEmail());
    }
}