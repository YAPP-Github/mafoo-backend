CREATE TABLE album(
    `id` CHAR(26) PRIMARY KEY NOT NULL COMMENT '앨범아이디',
    `name` VARCHAR(255) NOT NULL COMMENT '앨범이름',
    `type` VARCHAR(255) NOT NULL COMMENT '앨범타입',
    `owner_member_id` CHAR(26) NOT NULL COMMENT '앨범소유자아이디',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `album_idx1` (`owner_member_id`)
);
