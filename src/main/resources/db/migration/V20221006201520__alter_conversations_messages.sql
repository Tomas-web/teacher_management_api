alter table conversations_messages
    add constraint conversations_messages_conversations_id_fk
        foreign key (conversation_id) references conversations (id);
