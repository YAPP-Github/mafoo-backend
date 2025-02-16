CREATE TABLE notification_reservation(
    `notification_reservation_id` CHAR(26) PRIMARY KEY,
    `notification_template_id` CHAR(26) NOT NULL,
    `status` VARCHAR(64) NOT NULL,
    `variable_domain` VARCHAR(255) NOT NULL,
    `variable_type` VARCHAR(255) NOT NULL,
    `variable_sort` VARCHAR(255) NOT NULL,
    `receiver_member_ids` VARCHAR(255) NOT NULL,
    `send_at` TIMESTAMP NOT NULL,
    `send_repeat_interval` INTEGER DEFAULT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);