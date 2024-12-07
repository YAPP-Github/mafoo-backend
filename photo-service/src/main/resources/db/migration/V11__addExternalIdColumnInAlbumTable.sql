ALTER TABLE `album`
    ADD `external_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '외부 ID' AFTER `display_index`;
