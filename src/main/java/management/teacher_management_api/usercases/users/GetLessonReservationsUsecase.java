package management.teacher_management_api.usercases.users;

import lombok.RequiredArgsConstructor;
import management.teacher_management_api.usercases.dto.LessonReservation;
import management.teacher_management_api.ports.persistence.UsersReservationsDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetLessonReservationsUsecase {
    private final UsersReservationsDao usersReservationsDao;

    public List<LessonReservation> execute(long studentId, long teacherId) {
        return usersReservationsDao.findByTeacher(teacherId).stream()
                .filter(reservation -> reservation.getLessonEnd().isAfter(LocalDateTime.now()))
                .map(
                        row ->
                                LessonReservation.builder()
                                        .studentId(row.getStudentId())
                                        .teacherId(row.getTeacherId())
                                        .lessonStart(row.getLessonStart())
                                        .lessonEnd(row.getLessonEnd())
                                        .build())
                .collect(Collectors.toList());
    }
}
