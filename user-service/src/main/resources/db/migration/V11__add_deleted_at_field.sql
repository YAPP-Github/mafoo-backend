ALTER TABLE fcm_token
    ADD deleted_at TIMESTAMP NULL COMMENT '삭제여부' after updated_at;

ALTER TABLE notification
    ADD deleted_at TIMESTAMP NULL COMMENT '삭제여부' after updated_at;

ALTER TABLE notification_template
    ADD deleted_at TIMESTAMP NULL COMMENT '삭제여부' after updated_at;

ALTER TABLE notification_reservation
    ADD deleted_at TIMESTAMP NULL COMMENT '삭제여부' after updated_at;