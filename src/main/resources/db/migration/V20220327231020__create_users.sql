create table users
(
    id bigint auto_increment,
    external_url varchar(256) not null,
    full_name varchar(256) not null,
    email varchar(256) not null,
    role_id int not null,
    picture varchar(1024) null,
    description text null,
    price double null,
    address varchar(256) null,
    contacts text null,
    created_at timestamp not null,
    updated_at timestamp null,
    constraint users_pk
        primary key (id)
);
