package management.teacher_management_api.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.infrastructure.hibernate.entities.UserReview;
import management.teacher_management_api.ports.persistence.UsersReviewsDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRatingService {
    private final UsersReviewsDao usersReviewsDao;

    public double calculateUserRating(long userId) {
        val reviews = usersReviewsDao.listAll(userId);
        return reviews.stream().mapToDouble(UserReview::getReviewValue).average().orElse(0);
    }
}
