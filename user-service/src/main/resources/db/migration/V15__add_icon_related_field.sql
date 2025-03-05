alter table notification_reservation
    change variable_type variable_param varchar(255) null;

alter table notification_template
    change thumbnail_image_url icon varchar(255) not null;

alter table notification
    add icon varchar(255) null after receiver_member_id;