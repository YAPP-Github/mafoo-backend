CREATE TABLE album_export(
    `id` CHAR(26) PRIMARY KEY NOT NULL COMMENT '앨범내보내기아이디',
    `album_id` CHAR(26) NOT NULL COMMENT '앨범아이디',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '내보내기일시',
    INDEX `album_export_idx1` (`album_id`)
);

CREATE TABLE album_export_like(
    `export_id` CHAR(26) NOT NULL COMMENT '앨범내보내기아이디',
    `member_id` CHAR(26) NOT NULL COMMENT '멤버아이디',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`export_id`, `member_id`)
);

CREATE TABLE album_export_note(
    `id` CHAR(26) PRIMARY KEY NOT NULL COMMENT '앨범내보내기방명록아이디',
    `export_id` CHAR(26) NOT NULL COMMENT '앨범내보내기아이디',
    `type` VARCHAR(64) NOT NULL COMMENT '방명록타입',
    `member_id` CHAR(26) NOT NULL COMMENT '멤버아이디',
    `content` TEXT NOT NULL COMMENT '내용',
    `nickname` VARCHAR(64) NOT NULL COMMENT '닉네임',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `album_export_comment_idx1` (`export_id`),
    INDEX `album_export_comment_idx2` (`member_id`)
);
