package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.infrastructure.hibernate.entities.UserHomework;
import management.teacher_management_api.ports.persistence.UsersHomeworksDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class UsersHomeworksDaoImpl implements UsersHomeworksDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public List<UserHomework> findForTeacher(long teacherId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<UserHomework>)
                session.createSQLQuery(
                                "select * from users_homeworks where teacher_id = :teacherId")
                        .addEntity(UserHomework.class)
                        .setParameter("teacherId", teacherId)
                        .list();
    }

    @Override
    public List<UserHomework> findForStudent(long studentId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<UserHomework>)
                session.createSQLQuery(
                                "select * from users_homeworks where student_id = :studentId")
                        .addEntity(UserHomework.class)
                        .setParameter("studentId", studentId)
                        .list();
    }

    @Override
    public void save(long teacherId, long studentId, String content, LocalDateTime deadline) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "insert into users_homeworks(teacher_id, student_id, content, deadline, created_at) values(:teacherId, :studentId, :content, :deadline, :createdAt)")
                .setParameter("teacherId", teacherId)
                .setParameter("studentId", studentId)
                .setParameter("content", content)
                .setParameter("deadline", deadline)
                .setParameter("createdAt", LocalDateTime.now())
                .executeUpdate();
    }
}
