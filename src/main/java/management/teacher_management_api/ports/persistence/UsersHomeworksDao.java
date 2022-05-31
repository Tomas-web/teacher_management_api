package management.teacher_management_api.ports.persistence;

import management.teacher_management_api.infrastructure.hibernate.entities.UserHomework;

import java.time.LocalDateTime;
import java.util.List;

public interface UsersHomeworksDao {
    List<UserHomework> findForTeacher(long teacherId);
    List<UserHomework> findForStudent(long studentId);

    void save(long teacherId, long studentId, String content, LocalDateTime deadline);
}
