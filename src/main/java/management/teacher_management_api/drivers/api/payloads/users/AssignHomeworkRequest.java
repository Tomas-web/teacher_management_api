package management.teacher_management_api.drivers.api.payloads.users;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AssignHomeworkRequest {
    private String content;
    private LocalDateTime deadline;
}
