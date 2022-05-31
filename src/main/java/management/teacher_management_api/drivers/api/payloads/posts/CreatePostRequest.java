package management.teacher_management_api.drivers.api.payloads.posts;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreatePostRequest {
    private String title;
    private String content;
}
