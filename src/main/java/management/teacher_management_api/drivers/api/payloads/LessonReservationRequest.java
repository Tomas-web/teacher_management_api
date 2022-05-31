package management.teacher_management_api.drivers.api.payloads;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LessonReservationRequest {
    private LocalDateTime dateTime;
}
