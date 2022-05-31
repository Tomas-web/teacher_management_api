package management.teacher_management_api.drivers.api.payloads.users;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetStudentsResponse {
    private final List<User> students;
}
