package management.teacher_management_api.usercases.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LessonReservation {
    private final long teacherId;
    private final long studentId;
    private final LocalDateTime lessonStart;
    private final LocalDateTime lessonEnd;
}
