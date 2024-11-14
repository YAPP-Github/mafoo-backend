CREATE TABLE permission(
    `id` CHAR(26) PRIMARY KEY NOT NULL COMMENT '권한아이디',
    `type` VARCHAR(255) NOT NULL COMMENT '권한종류',
    `member_id` CHAR(26) NOT NULL COMMENT '권한대상사용자아이디',
    `album_id` CHAR(26) NOT NULL COMMENT '권한대상앨범아이디',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `permission_idx1` (`member_id`),
    INDEX `permission_idx2` (`album_id`)
);
