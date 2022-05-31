package management.teacher_management_api.usercases.posts;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.services.PostsFilteringService;
import management.teacher_management_api.domain.services.UserRatingService;
import management.teacher_management_api.ports.persistence.PostsDao;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.ports.persistence.UsersReviewsDao;
import management.teacher_management_api.usercases.dto.Post;
import management.teacher_management_api.usercases.dto.PostsWithPaging;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPostsUsecase {
    private static final long MAX_POSTS_NUM = 20;

    private final PostsFilteringService postsFilteringService;
    private final UserRatingService userRatingService;
    private final UsersReviewsDao usersReviewsDao;
    private final PostsDao postsDao;
    private final UsersDao usersDao;

    public PostsWithPaging execute(
            int pageNumber,
            Optional<String> q,
            Double lowestPrice,
            Double highestPrice,
            Optional<String> speciality,
            Optional<Double> rating,
            Optional<String> sortBy) {
        val postsIds =
                postsFilteringService.findPostsWithFilters(
                        q, lowestPrice, highestPrice, speciality, rating, sortBy);

        return PostsWithPaging.builder()
                .totalPages((int) Math.ceil(postsIds.size() / (double) MAX_POSTS_NUM))
                .posts(
                        postsIds.stream()
                                .skip((pageNumber - 1) * MAX_POSTS_NUM)
                                .limit(MAX_POSTS_NUM)
                                .map(
                                        postId -> {
                                            val post = postsDao.findById(postId);
                                            val user = usersDao.findById(post.getUserId());

                                            return Post.builder()
                                                    .id(post.getId())
                                                    .authorId(post.getUserId())
                                                    .title(post.getTitle())
                                                    .description(post.getContent())
                                                    .price(user.getPrice())
                                                    .rating(
                                                            userRatingService.calculateUserRating(
                                                                    post.getUserId()))
                                                    .reviewsCount(
                                                            usersReviewsDao
                                                                    .listAll(post.getUserId())
                                                                    .size())
                                                    .speciality(
                                                            usersDao.getSpeciality(
                                                                    post.getUserId()))
                                                    .createdAt(post.getCreatedAt())
                                                    .build();
                                        })
                                .collect(Collectors.toList()))
                .build();
    }
}
