package management.teacher_management_api.ports.persistence;

import management.teacher_management_api.infrastructure.hibernate.entities.PostView;

import java.time.LocalDateTime;
import java.util.List;

public interface PostViewsDao {
    List<PostView> findInPeriod(long postId, LocalDateTime periodStart, LocalDateTime periodEnd);
    List<PostView> findForUserInPeriod(long userId, LocalDateTime periodStart, LocalDateTime periodEnd);

    void save(long postId, int value, LocalDateTime date);
    void update(long postId, int value, LocalDateTime periodStart, LocalDateTime periodEnd);
}
