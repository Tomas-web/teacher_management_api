package management.teacher_management_api.usercases.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Reviewer {
    private final long id;
    private final String name;
    private final String avatarUrl;
}
