create table conversations
(
    id          bigint auto_increment,
    receiver_id bigint            not null,
    sender_id   bigint            not null,
    is_seen     tinyint default 0 not null,
    created_at  timestamp         not null,
    constraint conversations_pk
        primary key (id),
    constraint conversations_users_id_fk
        foreign key (receiver_id) references users (id),
    constraint conversations_users_id_fk_2
        foreign key (sender_id) references users (id)
);
