create table conversations_participants
(
    id              bigint auto_increment,
    participant_id  bigint not null,
    conversation_id bigint not null,
    is_seen         tinyint default 0 not null,
    constraint conversations_participants_pk
        primary key (id),
    constraint conversations_participants_conversations_id_fk
        foreign key (conversation_id) references conversations (id),
    constraint conversations_participants_users_id_fk
        foreign key (participant_id) references users (id)
);
