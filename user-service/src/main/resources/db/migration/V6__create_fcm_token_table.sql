CREATE TABLE fcm_token(
                          `fcm_token_id` CHAR(26) PRIMARY KEY,
                          `owner_member_id` CHAR(26) NOT NULL,
                          `token` VARCHAR(255) NOT NULL,
                          `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);