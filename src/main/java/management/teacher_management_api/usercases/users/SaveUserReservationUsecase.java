package management.teacher_management_api.usercases.users;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.exceptions.BadRequestException;
import management.teacher_management_api.drivers.api.exceptions.ConflictException;
import management.teacher_management_api.ports.persistence.UsersReservationsDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SaveUserReservationUsecase {
    private final UsersReservationsDao usersReservationsDao;

    public void execute(long userId, String teacherUid, LocalDateTime dateTime) {
        val teacherId = Long.parseLong(teacherUid);

        val reservation =
                usersReservationsDao.findByTeacher(teacherId).stream()
                        .filter(r -> r.getLessonStart().isEqual(dateTime))
                        .findFirst()
                        .orElse(null);

        if (reservation != null) {
            throw new BadRequestException();
        }

        val studentReservation =
                usersReservationsDao.findByStudent(userId).stream()
                        .filter(r -> r.getLessonStart().isEqual(dateTime))
                        .findFirst()
                        .orElse(null);

        if (studentReservation != null) {
            throw new ConflictException();
        }

        usersReservationsDao.save(teacherId, userId, dateTime, dateTime.plusHours(1));
    }
}
