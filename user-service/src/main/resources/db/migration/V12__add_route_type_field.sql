alter table notification_template
    drop column url;

alter table notification_template
    add routeType varchar(255) null after body;