package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.infrastructure.hibernate.entities.ConversationMessage;
import management.teacher_management_api.ports.persistence.ConversationsMessagesDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class ConversationsMessagesDaoImpl implements ConversationsMessagesDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public ConversationMessage getLatestMessage(long conversationId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (ConversationMessage)
                session.createSQLQuery(
                                "select * from conversations_messages where conversation_id = :conversationId order by created_at desc limit 1")
                        .addEntity(ConversationMessage.class)
                        .setParameter("conversationId", conversationId)
                        .uniqueResult();
    }

    @Override
    public List<ConversationMessage> listForConversation(long conversationId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<ConversationMessage>)
                session.createSQLQuery(
                                "select * from conversations_messages where conversation_id = :conversationId order by created_at desc")
                        .addEntity(ConversationMessage.class)
                        .setParameter("conversationId", conversationId)
                        .list();
    }

    @Override
    public ConversationMessage saveMessage(long conversationId, long senderId, String message) {
        val session = Utils.currentSession(entityManagerFactory);

        val conversationMessage = new ConversationMessage();
        conversationMessage.setConversationId(conversationId);
        conversationMessage.setSenderId(senderId);
        conversationMessage.setMessage(message);
        conversationMessage.setCreatedAt(LocalDateTime.now());

        session.save(conversationMessage);

        return conversationMessage;
    }
}
