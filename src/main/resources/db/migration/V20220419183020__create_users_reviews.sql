create table users_reviews
(
    id bigint auto_increment,
    target_user_id bigint not null,
    reviewer_id bigint not null,
    review_value int not null,
    comment text null,
    created_at timestamp not null,
    constraint users_reviews_pk
        primary key (id),
    constraint users_reviews_users_id_fk
        foreign key (target_user_id) references users (id),
    constraint users_reviews_users_id_fk_2
        foreign key (reviewer_id) references users (id)
);
