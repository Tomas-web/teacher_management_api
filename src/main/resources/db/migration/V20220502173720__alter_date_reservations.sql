alter table date_reservations change lesson lesson_end timestamp not null;

alter table date_reservations
    add created_at timestamp not null;
