package management.teacher_management_api.drivers.api.payloads.posts;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetPostsResponse {
    private final List<Post> posts;
    private final int totalPages;
}
