package management.teacher_management_api.usercases.users;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.ports.persistence.UsersReviewsDao;
import management.teacher_management_api.usercases.dto.Reviewer;
import management.teacher_management_api.usercases.dto.UserReview;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetReviewsUsecase {
    private final UsersReviewsDao usersReviewsDao;
    private final UsersDao usersDao;

    public List<UserReview> execute(String userUid) {
        val userId = Long.parseLong(userUid);
        val reviews = usersReviewsDao.listAll(userId);

        return reviews.stream()
                .map(
                        review -> {
                            val reviewer = usersDao.findById(review.getReviewerId());
                            return UserReview.builder()
                                    .id(review.getId())
                                    .value(review.getReviewValue())
                                    .reviewer(
                                            Reviewer.builder()
                                                    .id(reviewer.getId())
                                                    .name(reviewer.getFullName())
                                                    .avatarUrl(reviewer.getPicture())
                                                    .build())
                                    .comment(review.getComment())
                                    .createdAt(review.getCreatedAt())
                                    .build();
                        })
                .collect(Collectors.toList());
    }
}
