create table if not exists image
(
    id                 int auto_increment comment '图片id'
        primary key,
    user_id            int                                    not null comment '用户id',
    alias              varchar(255) default (now())           not null comment '图片别名',
    name               varchar(255)                           not null comment '图片名称',
    last_download_time timestamp    default CURRENT_TIMESTAMP not null comment '上一次下载的时间',
    constraint image_user_id_fk
        foreign key (user_id) references user (id)
)
    comment '图片表';