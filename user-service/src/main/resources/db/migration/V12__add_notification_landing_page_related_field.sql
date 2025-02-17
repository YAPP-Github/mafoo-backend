alter table notification_template
    drop column url;

alter table notification_template
    add route varchar(255) null after body;

alter table notification_template
    add route_key varchar(255) null after route;

alter table notification_template
    add has_button boolean not null after route_key;