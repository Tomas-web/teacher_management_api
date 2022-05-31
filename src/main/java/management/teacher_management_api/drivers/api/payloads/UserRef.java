package management.teacher_management_api.drivers.api.payloads;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRef {
    private final String id;
    private final String name;
    private final String avatarUrl;
}
