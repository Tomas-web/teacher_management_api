package management.teacher_management_api.domain;

import lombok.val;
import org.hibernate.Session;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;

import javax.persistence.EntityManagerFactory;

public abstract class Utils {
    public static Session currentSession(EntityManagerFactory entityManagerFactory) {
        val em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);

        if (em != null) {
            return em.unwrap(Session.class);
        } else {
            throw new IllegalStateException("No transaction is running");
        }
    }

    public static String generatePublicUid(String name) {
        return name.replace(" ", "-")
                .replace("@", "-")
                .replace(".", "-")
                .replace("/", "-")
                .toLowerCase();
    }
}
