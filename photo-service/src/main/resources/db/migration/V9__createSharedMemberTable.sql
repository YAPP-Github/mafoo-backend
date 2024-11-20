CREATE TABLE shared_member(
    `id` CHAR(26) PRIMARY KEY NOT NULL COMMENT '공유사용자아이디',
    `share_status` VARCHAR(255) NOT NULL COMMENT '공유상태',
    `permission_level` VARCHAR(255) NOT NULL COMMENT '공유단계',
    `member_id` CHAR(26) NOT NULL COMMENT '대상사용자아이디',
    `album_id` CHAR(26) NOT NULL COMMENT '대상앨범아이디',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `shared_album_member_idx1` (`member_id`),
    INDEX `shared_album_member_idx2` (`album_id`)
);
