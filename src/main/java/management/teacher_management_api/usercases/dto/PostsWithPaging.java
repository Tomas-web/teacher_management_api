package management.teacher_management_api.usercases.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostsWithPaging {
    private final List<Post> posts;
    private final int totalPages;
}
