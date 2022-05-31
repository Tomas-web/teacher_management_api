package management.teacher_management_api.ports.persistence;

import management.teacher_management_api.infrastructure.hibernate.entities.UserReview;

import java.util.List;

public interface UsersReviewsDao {
    List<UserReview> listAll(long userId);

    void save(long targetUserId, long reviewerId, int value, String comment);
}
