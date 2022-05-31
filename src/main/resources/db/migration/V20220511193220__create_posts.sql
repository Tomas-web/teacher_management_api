create table posts
(
    id bigint auto_increment,
    user_id bigint not null,
    title varchar(256) not null,
    content text not null,
    created_at timestamp null,
    constraint posts_pk
        primary key (id),
    constraint posts_users_id_fk
        foreign key (user_id) references users (id)
);
