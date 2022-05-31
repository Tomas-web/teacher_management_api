package management.teacher_management_api.drivers.api.payloads.users;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class UserReview {
    private final String id;
    private final Reviewer reviewer;
    private final int value;
    private final String comment;
    private final OffsetDateTime createdAt;
}
