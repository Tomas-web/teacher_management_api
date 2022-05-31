package management.teacher_management_api.usercases.posts;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.services.UserRatingService;
import management.teacher_management_api.ports.persistence.PostsDao;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.usercases.dto.Post;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserPostUsecase {
    private final PostsDao postsDao;
    private final UsersDao usersDao;
    private final UserRatingService userRatingService;

    public Post execute(long userId) {
        val post = postsDao.findForUser(userId);

        if (post == null) {
            return null;
        }

        return Post.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getContent())
                .speciality(usersDao.getSpeciality(userId))
                .price(usersDao.getPrice(userId))
                .authorId(userId)
                .rating(userRatingService.calculateUserRating(userId))
                .createdAt(post.getCreatedAt())
                .build();
    }
}
