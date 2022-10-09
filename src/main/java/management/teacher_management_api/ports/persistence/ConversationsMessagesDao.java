package management.teacher_management_api.ports.persistence;

import management.teacher_management_api.infrastructure.hibernate.entities.ConversationMessage;

import java.util.List;

public interface ConversationsMessagesDao {
    List<ConversationMessage> listForConversation(long conversationId);

    ConversationMessage getLatestMessage(long conversationId);

    ConversationMessage saveMessage(long conversationId, long senderId, String message);
}
