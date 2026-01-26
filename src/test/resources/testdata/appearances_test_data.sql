CREATE TABLE appearances (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    talent_id INT NOT NULL,
    date DATE NOT NULL,
    title VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    FOREIGN KEY (talent_id) REFERENCES talents(id)
);

INSERT INTO appearances (id, talent_id, date, title, text, created_at, updated_at, deleted_at) VALUES (1, 1, '2024-01-01', 'テストタイトル1', 'テスト本文1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO appearances (id, talent_id, date, title, text, created_at, updated_at, deleted_at) VALUES (2, 1, '2024-01-01', 'テストタイトル2', 'テスト本文2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO appearances (id, talent_id, date, title, text, created_at, updated_at, deleted_at) VALUES (3, 1, '2024-01-01', 'テストタイトル3', 'テスト本文3', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

ALTER TABLE appearances ALTER COLUMN id RESTART WITH 4;