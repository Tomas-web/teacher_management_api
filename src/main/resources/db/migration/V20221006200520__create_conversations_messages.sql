create table conversations_messages
(
    id              bigint auto_increment,
    conversation_id bigint    not null,
    sender_id       bigint    not null,
    message         text      not null,
    created_at      timestamp not null,
    constraint conversations_messages_pk
        primary key (id),
    constraint conversations_messages_users_id_fk
        foreign key (sender_id) references users (id)
);
