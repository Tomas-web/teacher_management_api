create table users_homeworks
(
    id bigint auto_increment,
    teacher_id bigint not null,
    student_id bigint not null,
    content text not null,
    deadline timestamp null,
    created_at timestamp null,
    constraint users_homeworks_pk
        primary key (id),
    constraint users_homeworks_users_id_fk
        foreign key (teacher_id) references users (id),
    constraint users_homeworks_users_id_fk_2
        foreign key (student_id) references users (id)
);
