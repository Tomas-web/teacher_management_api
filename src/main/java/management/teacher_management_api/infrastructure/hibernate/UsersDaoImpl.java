package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.domain.core.User;
import management.teacher_management_api.domain.core.UserRole;
import management.teacher_management_api.ports.persistence.UsersDao;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class UsersDaoImpl implements UsersDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public User findById(long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        return session.byId(User.class).load(userId);
    }

    @Override
    public User findByExternalId(String externalId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (User)
                session.createSQLQuery(
                                "select * from users where external_id = :externalId limit 1")
                        .addEntity(User.class)
                        .setParameter("externalId", externalId)
                        .uniqueResult();
    }

    @Override
    public String getSpeciality(long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (String)
                session.createSQLQuery(
                                "select name from specialities s "
                                        + "inner join users u on s.id = u.speciality_id "
                                        + "where u.id = :id")
                        .addScalar("name", StringType.INSTANCE)
                        .setParameter("id", userId)
                        .uniqueResult();
    }

    @Override
    public Double getPrice(long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (Double)
                session.createSQLQuery("select price from users where id = :id")
                        .addScalar("price", DoubleType.INSTANCE)
                        .setParameter("id", userId)
                        .uniqueResult();
    }

    @Override
    public List<User> listStudents(long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<User>)
                session.createSQLQuery(
                                "select distinct u.* from users u "
                                        + "inner join date_reservations dr on u.id = dr.student_id "
                                        + "where dr.teacher_id = :teacherId")
                        .addEntity(User.class)
                        .setParameter("teacherId", userId)
                        .list();
    }

    @Override
    public boolean isTeacher(long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        val count =
                (Integer)
                        session.createSQLQuery(
                                        "select count(*) as cnt from users where id = :userId and role_id = :roleId")
                                .addScalar("cnt", IntegerType.INSTANCE)
                                .setParameter("userId", userId)
                                .setParameter("roleId", UserRole.TEACHER.getId())
                                .uniqueResult();

        return count != null && count > 0;
    }

    @Override
    public void updatePicture(long userId, String url) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery("update users set picture = :picture where id = :id")
                .setParameter("picture", url)
                .setParameter("id", userId)
                .executeUpdate();
    }

    @Override
    public void saveOrUpdate(User user) {
        val session = Utils.currentSession(entityManagerFactory);
        session.saveOrUpdate(user);
    }
}
