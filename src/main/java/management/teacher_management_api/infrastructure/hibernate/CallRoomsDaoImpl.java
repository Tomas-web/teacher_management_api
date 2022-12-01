package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.infrastructure.hibernate.entities.CallRoom;
import management.teacher_management_api.ports.persistence.CallRoomsDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;

@Repository
@Transactional
@RequiredArgsConstructor
public class CallRoomsDaoImpl implements CallRoomsDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public CallRoom find(long callerId, long targetId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (CallRoom)
                session.createSQLQuery(
                                "select * from call_rooms where caller_id = :callerId and target_id = :targetId")
                        .addEntity(CallRoom.class)
                        .setParameter("callerId", callerId)
                        .setParameter("targetId", targetId)
                        .uniqueResult();
    }

    @Override
    public void create(long callerId, long targetId, String channelName, String token) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "insert into call_rooms(caller_id, target_id, name, token, created_at) values (:callerId, :targetId, :name, :token, :createdAt)")
                .setParameter("callerId", callerId)
                .setParameter("targetId", targetId)
                .setParameter("name", channelName)
                .setParameter("token", token)
                .setParameter("createdAt", LocalDateTime.now())
                .executeUpdate();
    }

    @Override
    public void delete(long callerId, long targetId) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "delete from call_rooms where caller_id = :callerId and target_id = :targetId")
                .setParameter("callerId", callerId)
                .setParameter("targetId", targetId)
                .executeUpdate();
    }
}
