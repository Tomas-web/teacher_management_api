package management.teacher_management_api.drivers.api.payloads;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Homework {
    private final String id;
    private final UserRef teacher;
    private final UserRef student;
    private final String content;
    private final OffsetDateTime deadline;
}
