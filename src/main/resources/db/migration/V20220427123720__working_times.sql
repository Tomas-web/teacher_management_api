create table working_times
(
    id bigint auto_increment,
    user_id bigint not null,
    time_start time not null,
    time_end time not null,
    created_at timestamp not null,
    constraint working_times_pk
        primary key (id),
    constraint working_times_users_id_fk
        foreign key (user_id) references users (id)
);
