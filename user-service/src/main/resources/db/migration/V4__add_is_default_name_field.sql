ALTER TABLE member
    ADD is_default_name TINYINT(1) DEFAULT FALSE AFTER name;
