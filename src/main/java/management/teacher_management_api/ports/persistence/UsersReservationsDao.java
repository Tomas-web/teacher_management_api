package management.teacher_management_api.ports.persistence;

import management.teacher_management_api.infrastructure.hibernate.entities.DateReservation;

import java.time.LocalDateTime;
import java.util.List;

public interface UsersReservationsDao {
    List<DateReservation> findByTeacher(long teacherId);
    List<DateReservation> findByStudent(long studentId);

    void save(long teacherId, long studentId, LocalDateTime start, LocalDateTime end);
}
