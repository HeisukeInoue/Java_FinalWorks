/*外部キー制約を無効化してテーブルを削除*/
SET REFERENTIAL_INTEGRITY FALSE;
DROP TABLE IF EXISTS ranking;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS blogs;
SET REFERENTIAL_INTEGRITY TRUE;

/*ブログテーブル*/
CREATE TABLE blogs (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    talent_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

INSERT INTO blogs (id, talent_id, title, text, created_at, updated_at, deleted_at)
VALUES (1, 1001, 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO blogs (id, talent_id, title, text, created_at, updated_at, deleted_at)
VALUES (2, 1001, 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO blogs (id, talent_id, title, text, created_at, updated_at, deleted_at)
VALUES (3, 1001, 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO blogs (id, talent_id, title, text, created_at, updated_at, deleted_at)
VALUES (4, 1001, 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO blogs (id, talent_id, title, text, created_at, updated_at, deleted_at)
VALUES (5, 1001, 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO blogs (id, talent_id, title, text, created_at, updated_at, deleted_at)
VALUES (6, 1001, 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO blogs (id, talent_id, title, text, created_at, updated_at, deleted_at)
VALUES (7, 1001, 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO blogs (id, talent_id, title, text, created_at, updated_at, deleted_at)
VALUES (8, 1001, 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO blogs (id, talent_id, title, text, created_at, updated_at, deleted_at)
VALUES (9, 1001, 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO blogs (id, talent_id, title, text, created_at, updated_at, deleted_at)
VALUES (10, 1001, 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO blogs (id, talent_id, title, text, created_at, updated_at, deleted_at)
VALUES (11, 1001, 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

/*AUTO_INCREMENTをリセット（H2用）*/
ALTER TABLE blogs ALTER COLUMN id RESTART WITH 12;

/*コメントテーブル*/
CREATE TABLE comments (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    blog_id INT NOT NULL,
    text TEXT NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (blog_id) REFERENCES blogs(id)
);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (1, 1, 'テストコメント1', 'テストユーザー1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (2, 1, 'テストコメント2', 'テストユーザー2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (3, 1, 'テストコメント3', 'テストユーザー3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (4, 1, 'テストコメント4', 'テストユーザー4', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (5, 1, 'テストコメント5', 'テストユーザー5', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (6, 1, 'テストコメント6', 'テストユーザー6', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (7, 1, 'テストコメント7', 'テストユーザー7', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (8, 1, 'テストコメント8', 'テストユーザー8', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (9, 1, 'テストコメント9', 'テストユーザー9', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (10, 1, 'テストコメント10', 'テストユーザー10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (11, 1, 'テストコメント11', 'テストユーザー11', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (12, 1, 'テストコメント12', 'テストユーザー12', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (13, 1, 'テストコメント13', 'テストユーザー13', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO comments (id, blog_id, text, created_by, created_at, updated_at, deleted_at)
VALUES (14, 1, 'テストコメント14', 'テストユーザー14', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

/*AUTO_INCREMENTをリセット（H2用）*/
ALTER TABLE comments ALTER COLUMN id RESTART WITH 15;

/*ランキングテーブル*/
CREATE TABLE ranking (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `rank` INT NOT NULL,
    blog_id INT,
    comment_count INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (blog_id) REFERENCES blogs(id)
);

INSERT INTO ranking (id, `rank`, blog_id, comment_count, created_at, updated_at, deleted_at) VALUES (1, 1, 1, 123, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO ranking (id, `rank`, blog_id, comment_count, created_at, updated_at, deleted_at) VALUES (2, 2, 3, 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO ranking (id, `rank`, blog_id, comment_count, created_at, updated_at, deleted_at) VALUES (3, 3, 9, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO ranking (id, `rank`, blog_id, comment_count, created_at, updated_at, deleted_at) VALUES (4, 4, 8, 22, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO ranking (id, `rank`, blog_id, comment_count, created_at, updated_at, deleted_at) VALUES (5, 5, 10, 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);
