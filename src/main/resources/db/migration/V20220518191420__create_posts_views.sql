create table posts_views
(
    id bigint auto_increment,
    post_id bigint not null,
    date timestamp null,
    num_of_views int null,
    constraint posts_views_pk
        primary key (id),
    constraint posts_views_posts_id_fk
        foreign key (post_id) references posts (id)
);
