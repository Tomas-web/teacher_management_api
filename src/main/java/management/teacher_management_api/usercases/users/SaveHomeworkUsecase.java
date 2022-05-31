package management.teacher_management_api.usercases.users;

import lombok.RequiredArgsConstructor;
import management.teacher_management_api.drivers.api.exceptions.ForbiddenException;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.ports.persistence.UsersHomeworksDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SaveHomeworkUsecase {
    private final UsersDao usersDao;
    private final UsersHomeworksDao usersHomeworksDao;

    public void execute(long teacherId, long studentId, String content, LocalDateTime deadline) {
        if (!usersDao.isTeacher(teacherId)) {
            throw new ForbiddenException();
        }

        usersHomeworksDao.save(teacherId, studentId, content, deadline);
    }
}
