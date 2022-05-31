package management.teacher_management_api.drivers.api.payloads.posts;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Post {
    private final String id;
    private final String title;
    private final String description;
    private final double price;
    private final double rating;
    private final int reviewsCount;
    private final String speciality;
    private final PostAuthor author;
    private final OffsetDateTime createdAt;
}
