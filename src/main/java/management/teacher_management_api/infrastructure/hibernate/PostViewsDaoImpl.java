package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.infrastructure.hibernate.entities.PostView;
import management.teacher_management_api.ports.persistence.PostViewsDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class PostViewsDaoImpl implements PostViewsDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public List<PostView> findInPeriod(
            long postId, LocalDateTime periodStart, LocalDateTime periodEnd) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<PostView>)
                session.createSQLQuery(
                                "select * from posts_views where post_id = :postId and date >= :periodStart and date < :periodEnd")
                        .addEntity(PostView.class)
                        .setParameter("postId", postId)
                        .setParameter("periodStart", periodStart)
                        .setParameter("periodEnd", periodEnd)
                        .list();
    }

    @Override
    public List<PostView> findForUserInPeriod(
            long userId, LocalDateTime periodStart, LocalDateTime periodEnd) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<PostView>)
                session.createSQLQuery(
                                "select pv.* from posts_views pv "
                                        + "inner join posts p on pv.post_id = p.id "
                                        + "where user_id = :userId and pv.date >= :periodStart and pv.date < :periodEnd")
                        .addEntity(PostView.class)
                        .setParameter("userId", userId)
                        .setParameter("periodStart", periodStart)
                        .setParameter("periodEnd", periodEnd)
                        .list();
    }

    @Override
    public void save(long postId, int value, LocalDateTime date) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "insert into posts_views(post_id, date, num_of_views) values(:postId, :date, :value)")
                .setParameter("postId", postId)
                .setParameter("date", date)
                .setParameter("value", value)
                .executeUpdate();
    }

    @Override
    public void update(long postId, int value, LocalDateTime periodStart, LocalDateTime periodEnd) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "update posts_views set num_of_views = :value where post_id = :postId and date >= :periodStart and date < :periodEnd")
                .setParameter("postId", postId)
                .setParameter("value", value)
                .setParameter("periodStart", periodStart)
                .setParameter("periodEnd", periodEnd)
                .executeUpdate();
    }
}
