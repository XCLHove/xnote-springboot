alter table image
    drop foreign key image_user_id_fk;

alter table image
    add constraint image_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete cascade;