package management.teacher_management_api.ports.persistence;

import management.teacher_management_api.infrastructure.hibernate.entities.CallRoom;

public interface CallRoomsDao {
    CallRoom find(long callerId, long targetId);
    void create(long callerId, long targetId, String channelName, String token);

    void delete(long callerId, long targetId);
}
