alter table notification_template
    add url VARCHAR(255) not null after body;