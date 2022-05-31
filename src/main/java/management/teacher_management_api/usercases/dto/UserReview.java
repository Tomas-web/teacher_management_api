package management.teacher_management_api.usercases.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserReview {
    private final long id;
    private final Reviewer reviewer;
    private final int value;
    private final String comment;
    private final LocalDateTime createdAt;
}
