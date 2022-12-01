create table call_rooms
(
    id         bigint auto_increment,
    caller_id  bigint       not null,
    target_id  bigint       not null,
    name       varchar(255) not null,
    token      varchar(255) not null,
    created_at timestamp    not null,
    constraint call_rooms_pk
        primary key (id),
    constraint call_rooms_users_id_fk
        foreign key (caller_id) references users (id),
    constraint call_rooms_users_id_fk_2
        foreign key (target_id) references users (id)
);
