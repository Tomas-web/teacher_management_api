package management.teacher_management_api.usercases.profile;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.ports.persistence.UsersReservationsDao;
import management.teacher_management_api.usercases.dto.LessonReservation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetProfileLessonReservationsUsecase {
    private final UsersReservationsDao usersReservationsDao;

    public List<LessonReservation> execute(long userId) {
        val reservations =
                usersReservationsDao.findByStudent(userId).stream()
                        .filter(
                                reservation ->
                                        reservation.getLessonEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());

        return reservations.stream()
                .map(
                        reservation ->
                                LessonReservation.builder()
                                        .teacherId(reservation.getTeacherId())
                                        .studentId(reservation.getStudentId())
                                        .lessonStart(reservation.getLessonStart())
                                        .lessonEnd(reservation.getLessonEnd())
                                        .build())
                .collect(Collectors.toList());
    }
}
