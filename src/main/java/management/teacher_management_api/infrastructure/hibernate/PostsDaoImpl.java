package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.infrastructure.hibernate.entities.Post;
import management.teacher_management_api.ports.persistence.PostsDao;
import org.hibernate.type.IntegerType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;

@Repository
@Transactional
@RequiredArgsConstructor
public class PostsDaoImpl implements PostsDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public Post findForUser(long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (Post)
                session.createSQLQuery("select * from posts where user_id = :userId limit 1")
                        .addEntity(Post.class)
                        .setParameter("userId", userId)
                        .uniqueResult();
    }

    @Override
    public Post findById(long id) {
        val session = Utils.currentSession(entityManagerFactory);

        return (Post)
                session.createSQLQuery("select * from posts where id = :id")
                        .addEntity(Post.class)
                        .setParameter("id", id)
                        .uniqueResult();
    }

    @Override
    public void delete(long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery("delete from posts where user_id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public void deleteById(long userId, long id) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery("delete from posts where user_id = :userId and id = :id")
                .setParameter("userId", userId)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void save(long userId, String title, String content) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "insert into posts(user_id, title, content, created_at) values (:userId, :title, :content, :createdAt)")
                .setParameter("userId", userId)
                .setParameter("title", title)
                .setParameter("content", content)
                .setParameter("createdAt", LocalDateTime.now())
                .executeUpdate();
    }
}
