ALTER TABLE member
    ADD deleted_at TIMESTAMP NULL COMMENT '삭제여부' after created_at;

ALTER TABLE social_member
    ADD deleted_at TIMESTAMP NULL COMMENT '삭제여부' after created_at;