package management.teacher_management_api.usercases.users;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.ports.persistence.UsersWorkingTimesDao;
import management.teacher_management_api.usercases.dto.UserWorkingTimes;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserWorkingTImesUsecase {
    private final UsersWorkingTimesDao usersWorkingTimesDao;

    public UserWorkingTimes execute(String userUid) {
        val userId = Long.parseLong(userUid);

        return UserWorkingTimes.builder()
                .timeStart(usersWorkingTimesDao.getTimeStart(userId))
                .timeEnd(usersWorkingTimesDao.getTimeEnd(userId))
                .build();
    }
}
