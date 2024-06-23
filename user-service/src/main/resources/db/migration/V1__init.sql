CREATE TABLE member(
    `member_id` CHAR(26) PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE social_member(
    `identity_provider` VARCHAR(64) NOT NULL,
    `identifier` VARCHAR(255) NOT NULL,
    `member_id` CHAR(26) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`identity_provider`, `identifier`)
);
