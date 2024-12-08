CREATE TABLE sumone_event_mapping(
      `sumone_id` VARCHAR(36) PRIMARY KEY NOT NULL COMMENT '썸원아이디',
      `invite_code` VARCHAR(64) NOT NULL COMMENT '초대 코드',
      `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      INDEX `sumone_event_mapping_idx1` (`invite_code`)
);
