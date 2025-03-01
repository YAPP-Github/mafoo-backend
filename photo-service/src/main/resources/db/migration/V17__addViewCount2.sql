ALTER TABLE album_export ADD COLUMN `view_count` BIGINT NOT NULL DEFAULT 0 COMMENT '조회수';
ALTER TABLE album_export_note DROP COLUMN `view_count`;
