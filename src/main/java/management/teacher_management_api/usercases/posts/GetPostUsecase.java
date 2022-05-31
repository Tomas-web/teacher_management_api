package management.teacher_management_api.usercases.posts;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.services.UserRatingService;
import management.teacher_management_api.drivers.api.exceptions.NotFoundException;
import management.teacher_management_api.ports.persistence.PostsDao;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.usercases.dto.Post;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetPostUsecase {
    private final PostsDao postsDao;
    private final UsersDao usersDao;
    private final UserRatingService userRatingService;

    public Post execute(long postId) {
        val post = postsDao.findById(postId);

        if (post == null) {
            throw new NotFoundException();
        }

        return Post.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getContent())
                .speciality(usersDao.getSpeciality(post.getUserId()))
                .price(usersDao.getPrice(post.getUserId()))
                .authorId(post.getUserId())
                .rating(userRatingService.calculateUserRating(post.getUserId()))
                .createdAt(post.getCreatedAt())
                .build();
    }
}
