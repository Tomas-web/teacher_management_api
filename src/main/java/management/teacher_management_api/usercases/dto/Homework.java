package management.teacher_management_api.usercases.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Homework {
    private final long id;
    private final long teacherId;
    private final long studentId;
    private final String content;
    private final LocalDateTime deadline;
}
