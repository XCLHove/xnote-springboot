drop table if exists admin;
#-------------------------------------------------------------
create table if not exists note_type
(
    id      int auto_increment comment '类型ID'
        primary key,
    name    varchar(255) not null comment '类型名称',
    user_id int          not null comment '用户ID',
    constraint note_type_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete cascade
)
    comment '笔记分类';
#-------------------------------------------------------------
create table if not exists share_note_record
(
    id          int auto_increment
        primary key,
    code        varchar(255)                        not null comment '分享码',
    note_id     int                                 not null comment '笔记ID',
    user_id     int                                 not null comment '用户ID',
    expire_time datetime default current_timestamp not null comment '过期时间',
    constraint share_note_pk unique (code),
    constraint share_note_note_id_fk
        foreign key (note_id) references note (id)
            on update cascade on delete cascade,
    constraint share_note_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete cascade
);
#-------------------------------------------------------------
alter table note
    drop column access_code;
alter table note
    drop column keywords;
alter table note
    drop key note_title_user_id_uindex;

alter table note
    modify title varchar(255) default (now()) not null comment ' 笔记标题';
alter table note
    modify content text null comment '笔记内容';
alter table note
    add type_id int null comment '类型ID';
alter table note
    add constraint note_note_type_id_fk
        foreign key (type_id) references note_type (id)
            on update cascade on delete restrict;
#-------------------------------------------------------------
alter table user
    add home_page_note_id int null comment '用户主页要显示的笔记的ID';
alter table user
    add constraint user_note_id_fk
        foreign key (home_page_note_id) references note (id)
            on update set null on delete set null;
alter table user
    add image_storage_size
        bigint default 104857600 null comment '图片存储空间大小（默认100MB）';
#-------------------------------------------------------------
create table if not exists user_image
(
    id       int auto_increment primary key,
    user_id  int          not null comment '用户ID',
    image_id int          not null comment '图片ID',
    alias    varchar(255) not null comment '图片别名',
    constraint user_image_pk
        unique (user_id, image_id),
    constraint user_image_image_id_fk
        foreign key (image_id) references image (id)
            on update cascade on delete cascade,
    constraint user_image_user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete cascade
);

#  迁移数据
insert into user_image (user_id, image_id, alias)
select user_id, id, alias from image;
#-------------------------------------------------------------
alter table image drop column alias;
alter table image drop foreign key image_user_id_fk;
alter table image drop column user_id;

alter table image
    add size bigint not null comment '图片文件大小（bit）';
alter table image
    add owner_count int default 1 not null comment '拥有（上传）该图片的人数';

alter table image
    add constraint image_pk unique (name);
#-------------------------------------------------------------