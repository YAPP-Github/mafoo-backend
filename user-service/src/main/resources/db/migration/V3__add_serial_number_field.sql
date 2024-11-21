ALTER TABLE member
    ADD serial_number INT UNSIGNED NULL AFTER member_id;


SET @row_number = 0;

UPDATE member
SET serial_number = (@row_number := @row_number + 1)
    ORDER BY created_at;


SELECT MAX(serial_number) + 1 INTO @next_serial FROM member;

ALTER TABLE member
    MODIFY serial_number INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE;
