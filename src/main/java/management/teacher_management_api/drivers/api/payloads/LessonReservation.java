package management.teacher_management_api.drivers.api.payloads;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class LessonReservation {
    private final String teacherId;
    private final String studentId;
    private final OffsetDateTime lessonStart;
    private final OffsetDateTime lessonEnd;
}
