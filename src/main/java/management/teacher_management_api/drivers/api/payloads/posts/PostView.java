package management.teacher_management_api.drivers.api.payloads.posts;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class PostView {
    private final int value;
    private final OffsetDateTime date;
}
