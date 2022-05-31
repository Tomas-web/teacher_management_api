package management.teacher_management_api.drivers.api.payloads;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
public class LessonReservationsResponse {
    private final List<LessonReservation> lessonReservations;
}
