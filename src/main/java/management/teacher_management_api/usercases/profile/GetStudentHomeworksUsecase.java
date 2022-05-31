package management.teacher_management_api.usercases.profile;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.core.HomeworkListType;
import management.teacher_management_api.ports.persistence.UsersHomeworksDao;
import management.teacher_management_api.usercases.dto.Homework;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetStudentHomeworksUsecase {
    private final UsersHomeworksDao usersHomeworksDao;

    public List<Homework> execute(long studentId, HomeworkListType type) {
        val homeworks = usersHomeworksDao.findForStudent(studentId);

        val result =
                homeworks.stream()
                        .map(
                                userHomework ->
                                        Homework.builder()
                                                .id(userHomework.getId())
                                                .teacherId(userHomework.getTeacherId())
                                                .studentId(userHomework.getStudentId())
                                                .content(userHomework.getContent())
                                                .deadline(userHomework.getDeadline())
                                                .build())
                        .collect(Collectors.toList());

        if (type == HomeworkListType.UPCOMING) {
            val now = LocalDateTime.now();
            return result.stream()
                    .filter(homework -> homework.getDeadline().isAfter(now))
                    .collect(Collectors.toList());
        }

        return result;
    }
}
