package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.ports.persistence.SpecialitiesDao;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class SpecialitiesDaoImpl implements SpecialitiesDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public List<String> listAll() {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<String>)
                session.createSQLQuery("select name from specialities order by name")
                        .addScalar("name", StringType.INSTANCE)
                        .list();
    }

    @Override
    public Long findByName(String name) {
        val session = Utils.currentSession(entityManagerFactory);

        return (Long)
                session.createSQLQuery("select id from specialities where name = :name")
                        .addScalar("id", LongType.INSTANCE)
                        .setParameter("name", name)
                        .uniqueResult();
    }
}
