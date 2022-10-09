package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.infrastructure.hibernate.entities.Conversation;
import management.teacher_management_api.infrastructure.hibernate.entities.ConversationParticipant;
import management.teacher_management_api.ports.persistence.ConversationsDao;
import org.hibernate.type.BooleanType;
import org.hibernate.type.LongType;
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
    public Conversation find(long receiverId, long senderId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (Conversation)
                session.createSQLQuery(
                                "select c.* from conversations c where "
                                        + "(select count(*) from conversations_participants where c.id = conversation_id and (participant_id = :receiverId or participant_id = :senderId)) = 2")
                        .addEntity(Conversation.class)
                        .setParameter("receiverId", receiverId)
                        .setParameter("senderId", senderId)
                        .uniqueResult();
    }

    @Override
    public List<Conversation> listAll(long receiverId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<Conversation>)
                session.createSQLQuery(
                                "select c.* from conversations c "
                                        + "inner join conversations_participants cp on c.id = cp.conversation_id "
                                        + "where cp.participant_id = :receiverId")
                        .addEntity(Conversation.class)
                        .setParameter("receiverId", receiverId)
                        .list();
    }

    @Override
    public long getSecondParticipantId(long conversationId, long participantId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (Long)
                session.createSQLQuery(
                                "select participant_id from conversations_participants where conversation_id = :conversationId and participant_id <> :participantId")
                        .addScalar("participant_id", LongType.INSTANCE)
                        .setParameter("conversationId", conversationId)
                        .setParameter("participantId", participantId)
                        .uniqueResult();
    }

    @Override
    public boolean isSeen(long conversationId, long participantId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (Boolean)
                session.createSQLQuery(
                                "select is_seen from conversations_participants where conversation_id = :conversationId and participant_id = :participantId")
                        .addScalar("is_seen", BooleanType.INSTANCE)
                        .setParameter("conversationId", conversationId)
                        .setParameter("participantId", participantId)
                        .uniqueResult();
    }

    @Override
    public void markSeen(long conversationId, long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "update conversations_participants set is_seen = 1 where conversation_id = :conversationId and participant_id = :userId")
                .setParameter("conversationId", conversationId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public void markUnseen(long conversationId, long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "update conversations_participants set is_seen = 0 where conversation_id = :conversationId and participant_id = :userId")
                .setParameter("conversationId", conversationId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public Conversation create(long receiverId, long senderId) {
        val session = Utils.currentSession(entityManagerFactory);

        val conversation = new Conversation();
        conversation.setCreatedAt(LocalDateTime.now());

        session.save(conversation);

        val receiver = new ConversationParticipant();
        receiver.setConversationId(conversation.getId());
        receiver.setParticipantId(receiverId);
        receiver.setSeen(false);

        session.save(receiver);

        val sender = new ConversationParticipant();
        sender.setConversationId(conversation.getId());
        sender.setParticipantId(senderId);
        sender.setSeen(true);

        session.save(sender);

        return conversation;
    }
}
