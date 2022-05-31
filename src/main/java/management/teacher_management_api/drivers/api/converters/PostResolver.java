package management.teacher_management_api.drivers.api.converters;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.payloads.posts.Post;
import management.teacher_management_api.drivers.api.payloads.posts.PostAuthor;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.utils.DateTimeUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostResolver {
    private final UsersDao usersDao;

    public Post toDTO(management.teacher_management_api.usercases.dto.Post data) {
        val user = usersDao.findById(data.getAuthorId());

        return Post.builder()
                .id(Long.toString(data.getId()))
                .title(data.getTitle())
                .description(data.getDescription())
                .price(data.getPrice())
                .speciality(data.getSpeciality())
                .rating(data.getRating())
                .reviewsCount(data.getReviewsCount())
                .author(
                        PostAuthor.builder()
                                .id(Long.toString(user.getId()))
                                .name(user.getFullName())
                                .avatarUrl(user.getPicture())
                                .build())
                .createdAt(DateTimeUtils.toOffsetDateTime(data.getCreatedAt()))
                .build();
    }
}
