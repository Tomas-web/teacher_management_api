package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.ports.persistence.UsersWorkingTimesDao;
import org.hibernate.type.LocalDateTimeType;
import org.hibernate.type.LocalTimeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Repository
@Transactional
@RequiredArgsConstructor
public class UsersWorkingTimesDaoImpl implements UsersWorkingTimesDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public LocalTime getTimeStart(long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (LocalTime)
                session.createSQLQuery(
                                "select time_start from working_times where user_id = :userId")
                        .addScalar("time_start", LocalTimeType.INSTANCE)
                        .setParameter("userId", userId)
                        .uniqueResult();
    }

    @Override
    public LocalTime getTimeEnd(long userId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (LocalTime)
                session.createSQLQuery("select time_end from working_times where user_id = :userId")
                        .addScalar("time_end", LocalTimeType.INSTANCE)
                        .setParameter("userId", userId)
                        .uniqueResult();
    }

    @Override
    public void saveWorkingTimes(long userId, LocalTime timeStart, LocalTime timeEnd) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "insert into working_times(user_id, time_start, time_end, created_at) values (:userId, :timeStart, :timeEnd, :createdAt)")
                .setParameter("userId", userId)
                .setParameter("timeStart", timeStart)
                .setParameter("timeEnd", timeEnd)
                .setParameter("createdAt", LocalDateTime.now())
                .executeUpdate();
    }

    @Override
    public void updateTimeStart(long userId, LocalTime time) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "update working_times set time_start = :timeStart where user_id = :userId")
                .setParameter("timeStart", time)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public void updateTimeEnd(long userId, LocalTime time) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "update working_times set time_end = :timeEnd where user_id = :userId")
                .setParameter("timeEnd", time)
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
