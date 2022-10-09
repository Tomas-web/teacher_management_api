alter table conversations
    drop column is_seen;

alter table conversations
    drop foreign key conversations_users_id_fk;

alter table conversations
    drop foreign key conversations_users_id_fk_2;

drop index conversations_users_id_fk on conversations;

drop index conversations_users_id_fk_2 on conversations;

alter table conversations
    drop column receiver_id;

alter table conversations
    drop column sender_id;

