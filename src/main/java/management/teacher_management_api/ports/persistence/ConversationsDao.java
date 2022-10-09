package management.teacher_management_api.ports.persistence;

import management.teacher_management_api.infrastructure.hibernate.entities.Conversation;

import java.util.List;

public interface ConversationsDao {
    Conversation find(long receiverId, long senderId);

    List<Conversation> listAll(long receiverId);

    long getSecondParticipantId(long conversationId, long participantId);

    boolean isSeen(long conversationId, long participantId);

    void markSeen(long conversationId, long userId);

    void markUnseen(long conversationId, long userId);

    Conversation create(long receiverId, long senderId);
}
