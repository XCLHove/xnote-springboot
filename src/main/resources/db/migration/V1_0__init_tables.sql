create table if not exists admin
(
    id       int auto_increment comment '管理员id'
        primary key,
    name     varchar(30)  not null comment '管理员名',
    account  varchar(30)  not null comment '账号',
    password varchar(255) not null comment '密码',
    email    varchar(50)  not null comment '邮箱'
);

create table if not exists user
(
    id       int auto_increment comment '用户id'
        primary key,
    name     varchar(30)       not null comment '用户名',
    account  varchar(30)       not null comment '账号',
    password varchar(128)      not null comment '密码',
    email    varchar(50)       null comment '邮箱',
    status   tinyint default 1 not null comment '用户状(1:正常 2：已禁封)',
    constraint user_pk
        unique (account)
);

create table if not exists note
(
    id       int auto_increment
        primary key,
    title    varchar(30) default (now()) not null comment '标题',
    content  text                        null comment '内容',
    keywords text                        null comment '关键词',
    user_id  int                         not null comment '所属用户id',
    constraint note_title_user_id_uindex
        unique (title, user_id),
    constraint user_id_fk
        foreign key (user_id) references user (id)
            on update cascade on delete cascade
);
