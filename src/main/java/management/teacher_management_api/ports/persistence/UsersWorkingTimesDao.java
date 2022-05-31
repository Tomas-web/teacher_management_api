package management.teacher_management_api.ports.persistence;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface UsersWorkingTimesDao {
    LocalTime getTimeStart(long userId);
    LocalTime getTimeEnd(long userId);

    void saveWorkingTimes(long userId, LocalTime timeStart, LocalTime timeEnd);
    void updateTimeStart(long userId, LocalTime time);
    void updateTimeEnd(long userId, LocalTime time);
}
