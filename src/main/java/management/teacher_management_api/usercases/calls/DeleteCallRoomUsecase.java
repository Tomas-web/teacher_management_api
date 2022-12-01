package management.teacher_management_api.usercases.calls;

import lombok.RequiredArgsConstructor;
import management.teacher_management_api.ports.persistence.CallRoomsDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCallRoomUsecase {
    private final CallRoomsDao callRoomsDao;

    public void execute(long userId, long targetId) {
        callRoomsDao.delete(userId, targetId);
    }
}
