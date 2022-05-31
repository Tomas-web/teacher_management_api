create table date_reservations
(
    id bigint auto_increment,
    teacher_id bigint not null,
    student_id bigint not null,
    lesson_start timestamp not null,
    constraint date_reservation_pk
        primary key (id),
    constraint date_reservations_users_id_fk
        foreign key (teacher_id) references users (id),
    constraint date_reservations_users_id_fk_2
        foreign key (student_id) references users (id)
);
