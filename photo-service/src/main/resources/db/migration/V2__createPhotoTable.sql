CREATE TABLE photo(
    `id` CHAR(26) PRIMARY KEY NOT NULL COMMENT '사진아이디',
    `url` VARCHAR(255) NOT NULL COMMENT '사진url',
    `owner_member_id` CHAR(26) NOT NULL COMMENT '사진소유자아이디',
    `album_id` CHAR(26) NULL COMMENT '사진앨범아이디',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `photo_idx1` (`owner_member_id`),
    INDEX `photo_idx2` (`album_id`)
);
