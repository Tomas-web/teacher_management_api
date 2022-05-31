package management.teacher_management_api.usercases.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class UserWorkingTimes {
    private final LocalTime timeStart;
    private final LocalTime timeEnd;
}
