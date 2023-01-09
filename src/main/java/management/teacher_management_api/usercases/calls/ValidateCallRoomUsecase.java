package management.teacher_management_api.usercases.calls;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.ports.persistence.CallRoomsDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateCallRoomUsecase {
    private final CallRoomsDao callRoomsDao;

    public boolean execute(
            long userId, long callerId, long targetId, String token, String channelName) {
        val callRoom = callRoomsDao.find(callerId, targetId);

        if (callRoom == null) {
            return false;
        }

        if (callRoom.getCallerId() != userId && callRoom.getTargetId() != userId) {
            return false;
        }

        return callRoom.getToken().equals(token)
                && callRoom.getName().equals(channelName);
    }
}
