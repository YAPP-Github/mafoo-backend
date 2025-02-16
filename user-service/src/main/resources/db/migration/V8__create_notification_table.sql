CREATE TABLE notification(
                             `notification_id` CHAR(26) PRIMARY KEY,
                             `notification_template_id` CHAR(26) NOT NULL,
                             `receiver_member_id` CHAR(26) NOT NULL,
                             `title` VARCHAR(255) NOT NULL,
                             `body` VARCHAR(255) NOT NULL,
                             `is_read` tinyint(1) NOT NULL DEFAULT 0,
                             `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);