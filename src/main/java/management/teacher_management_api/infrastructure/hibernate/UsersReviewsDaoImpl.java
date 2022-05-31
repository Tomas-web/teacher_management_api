package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.infrastructure.hibernate.entities.UserReview;
import management.teacher_management_api.ports.persistence.UsersReviewsDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class UsersReviewsDaoImpl implements UsersReviewsDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public List<UserReview> listAll(long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<UserReview>)
                session.createSQLQuery("select * from users_reviews where target_user_id = :userId order by created_at desc")
                        .addEntity(UserReview.class)
                        .setParameter("userId", userId)
                        .list();
    }

    @Override
    public void save(long targetUserId, long reviewerId, int value, String comment) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "insert into users_reviews(target_user_id, reviewer_id, review_value, comment, created_at) "
                                + "values (:targetUserId, :reviewerId, :value, :comment, :createdAt)")
                .setParameter("targetUserId", targetUserId)
                .setParameter("reviewerId", reviewerId)
                .setParameter("value", value)
                .setParameter("comment", comment)
                .setParameter("createdAt", LocalDateTime.now())
                .executeUpdate();
    }
}
