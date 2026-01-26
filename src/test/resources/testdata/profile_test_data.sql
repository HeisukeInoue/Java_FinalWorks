create table talents (
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    image_link VARCHAR(500) NOT NULL,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    `from` VARCHAR(255) NOT NULL,
    height INT NOT NULL,
    hobby VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

INSERT INTO talents (id, image_link, name, age, `from`, height, hobby, created_at, updated_at, deleted_at) VALUES (1, 'https://example.com/image.jpg', 'テストタレント1', 20, '東京', 170, 'テストタレント1の趣味', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);

INSERT INTO talents (id, image_link, name, age, `from`, height, hobby, created_at, updated_at, deleted_at) VALUES (2, 'https://example.com/image2.jpg', 'テストタレント2', 21, '大阪', 175, 'テストタレント2の趣味', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL);