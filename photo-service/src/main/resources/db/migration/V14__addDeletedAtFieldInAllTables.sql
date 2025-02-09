ALTER TABLE `photo`
    ADD `deleted_at` TIMESTAMP NULL COMMENT '삭제여부' after `updated_at`;

ALTER TABLE `album`
    ADD `deleted_at` TIMESTAMP NULL COMMENT '삭제여부' after `updated_at`;

ALTER TABLE `shared_member`
    ADD `deleted_at` TIMESTAMP NULL COMMENT '삭제여부' after `updated_at`;