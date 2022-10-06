package management.teacher_management_api.ports.persistence;

import management.teacher_management_api.infrastructure.hibernate.entities.Conversation;
import management.teacher_management_api.infrastructure.hibernate.entities.ConversationMessage;

import java.util.List;

public interface ConversationsDao {
    List<Conversation> listAll(long receiverId);

    ConversationMessage getLatestMessage(long conversationId);

    void markSeen(long conversationId);

    Conversation create(long receiverId, long senderId, boolean isSeen);

    void saveMessage(long conversationId, long senderId, String message);
}
