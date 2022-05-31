package management.teacher_management_api.usercases.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostView {
    private final int value;
    private final LocalDateTime date;
}
