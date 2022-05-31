package management.teacher_management_api.drivers.api.payloads;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetTeacherHomeworksResponse {
    private final List<Homework> homeworks;
}
