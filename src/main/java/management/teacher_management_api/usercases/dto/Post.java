package management.teacher_management_api.usercases.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Post {
    private final long id;
    private final String title;
    private final String description;
    private final double price;
    private final double rating;
    private final int reviewsCount;
    private final String speciality;
    private final long authorId;
    private final LocalDateTime createdAt;
}
