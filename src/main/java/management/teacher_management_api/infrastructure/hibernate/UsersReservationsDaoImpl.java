package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.infrastructure.hibernate.entities.DateReservation;
import management.teacher_management_api.ports.persistence.UsersReservationsDao;
import org.hibernate.type.LocalDateTimeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class UsersReservationsDaoImpl implements UsersReservationsDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public List<DateReservation> findByTeacher(long teacherId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<DateReservation>)
                session.createSQLQuery(
                                "select * from date_reservations where teacher_id = :teacherId order by lesson_start")
                        .addEntity(DateReservation.class)
                        .setParameter("teacherId", teacherId)
                        .list();
    }

    @Override
    public List<DateReservation> findByStudent(long studentId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<DateReservation>)
                session.createSQLQuery(
                                "select * from date_reservations where student_id = :studentId order by lesson_start")
                        .addEntity(DateReservation.class)
                        .setParameter("studentId", studentId)
                        .list();
    }

    @Override
    public void save(long teacherId, long studentId, LocalDateTime start, LocalDateTime end) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "insert into date_reservations(teacher_id, student_id, lesson_start, lesson_end, created_at) values (:teacherId, :studentId, :start, :end, :createdAt)")
                .setParameter("teacherId", teacherId)
                .setParameter("studentId", studentId)
                .setParameter("start", start)
                .setParameter("end", end)
                .setParameter("createdAt", LocalDateTime.now())
                .executeUpdate();
    }
}
