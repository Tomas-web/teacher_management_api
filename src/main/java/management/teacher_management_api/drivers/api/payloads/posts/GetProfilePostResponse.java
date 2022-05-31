package management.teacher_management_api.drivers.api.payloads.posts;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetProfilePostResponse {
    private final Post post;
}
