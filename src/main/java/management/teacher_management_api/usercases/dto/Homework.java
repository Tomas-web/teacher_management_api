package management.teacher_management_api.usercases.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Homework {
    private final long id;
    private final long teacherId;
    private final long studentId;
    private final String content;
    private final List<HomeworkUpload> uploads;
    private final LocalDateTime deadline;
}
