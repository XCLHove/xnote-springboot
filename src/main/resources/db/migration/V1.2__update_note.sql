alter table note
    add release_time timestamp default now() not null comment '发布时间';

alter table note
    add is_public int default 1 not null comment '是否公开(0:否，1：是，默认否)';

alter table note
    add update_time timestamp default now() not null comment '更新时间';

alter table note
    add access_code char(30) default '' comment '访问码';
