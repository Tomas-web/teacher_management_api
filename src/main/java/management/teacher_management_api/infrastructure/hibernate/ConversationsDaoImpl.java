package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.infrastructure.hibernate.entities.Conversation;
import management.teacher_management_api.infrastructure.hibernate.entities.ConversationMessage;
import management.teacher_management_api.ports.persistence.ConversationsDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class ConversationsDaoImpl implements ConversationsDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public List<Conversation> listAll(long receiverId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<Conversation>)
                session.createSQLQuery(
                                "select * from conversations where receiver_id = :receiverId")
                        .addEntity(Conversation.class)
                        .setParameter("receiverId", receiverId)
                        .list();
    }

    @Override
    public ConversationMessage getLatestMessage(long conversationId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (ConversationMessage)
                session.createSQLQuery(
                                "select * from conversations_messages where conversation_id = :conversationId order by created_at desc ")
                        .addEntity(ConversationMessage.class)
                        .setParameter("conversationId", conversationId)
                        .setMaxResults(1)
                        .uniqueResult();
    }

    @Override
    public void markSeen(long conversationId) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery("update conversations set is_seen = 1 where id = :conversationId")
                .setParameter("conversationId", conversationId)
                .executeUpdate();
    }

    @Override
    public Conversation create(long receiverId, long senderId, boolean isSeen) {
        val session = Utils.currentSession(entityManagerFactory);

        val conversation = new Conversation();
        conversation.setReceiverId(receiverId);
        conversation.setSenderId(senderId);
        conversation.setSeen(isSeen);
        conversation.setCreatedAt(LocalDateTime.now());

        session.save(conversation);

        return conversation;
    }

    @Override
    public void saveMessage(long conversationId, long senderId, String message) {
        val session = Utils.currentSession(entityManagerFactory);

        val conversationMessage = new ConversationMessage();
        conversationMessage.setConversationId(conversationId);
        conversationMessage.setSenderId(senderId);
        conversationMessage.setMessage(message);
        conversationMessage.setCreatedAt(LocalDateTime.now());

        session.save(conversationMessage);
    }
}
