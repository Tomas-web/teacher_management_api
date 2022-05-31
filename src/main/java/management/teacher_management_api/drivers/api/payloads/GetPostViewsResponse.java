package management.teacher_management_api.drivers.api.payloads;

import lombok.Builder;
import lombok.Data;
import management.teacher_management_api.drivers.api.payloads.posts.PostView;

import java.util.List;

@Data
@Builder
public class GetPostViewsResponse {
    private final List<PostView> postViews;
}
