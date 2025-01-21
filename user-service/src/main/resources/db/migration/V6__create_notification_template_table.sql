CREATE TABLE notification_template(
                                      `notification_template_id` CHAR(26) PRIMARY KEY,
                                      `notification_type` VARCHAR(64) NOT NULL,
                                      `thumbnail_image_url` VARCHAR(255) NOT NULL,
                                      `title` VARCHAR(255) NOT NULL,
                                      `body` VARCHAR(255) NOT NULL,
                                      `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);