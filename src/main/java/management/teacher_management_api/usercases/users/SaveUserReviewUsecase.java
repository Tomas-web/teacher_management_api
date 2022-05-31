package management.teacher_management_api.usercases.users;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.ports.persistence.UsersReviewsDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveUserReviewUsecase {
    private final UsersReviewsDao usersReviewsDao;

    public void execute(long reviewerId, String userUid, String comment, int value) {
        val userId = Long.parseLong(userUid);
        usersReviewsDao.save(userId, reviewerId, value, comment);
    }
}
