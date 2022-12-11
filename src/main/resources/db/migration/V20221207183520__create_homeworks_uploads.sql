create table homeworks_uploads
(
    id          bigint auto_increment,
    homework_id bigint       not null,
    user_id     bigint       not null,
    file_name   varchar(255) not null,
    file_size   bigint       not null,
    uploaded_at timestamp    not null,
    constraint homeworks_uploads_pk
        primary key (id),
    constraint homeworks_uploads_users_homeworks_id_fk
        foreign key (homework_id) references users_homeworks (id),
    constraint homeworks_uploads_users_id_fk
        foreign key (user_id) references users (id)
);
