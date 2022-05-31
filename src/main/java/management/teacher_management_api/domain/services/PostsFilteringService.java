package management.teacher_management_api.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.domain.core.PostsSortingType;
import management.teacher_management_api.domain.core.UserRole;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostsFilteringService {
    private final EntityManagerFactory entityManagerFactory;

    @Transactional
    public List<Long> findPostsWithFilters(
            Optional<String> q,
            Double lowestPrice,
            Double highestPrice,
            Optional<String> speciality,
            Optional<Double> rating,
            Optional<String> sortBy) {
        val session = Utils.currentSession(entityManagerFactory);

        String queryStr =
                "select p.id from posts p "
                        + "inner join users u on p.user_id = u.id "
                        + "inner join specialities s on u.speciality_id = s.id "
                        + "where u.price >= :lowestPrice and u.price <= :highestPrice and u.role_id = :userRoleId ";

        if (q.isPresent()) {
            queryStr += "and (p.title like :q or p.content like :q or u.full_name like :q) ";
        }

        if (speciality.isPresent()) {
            queryStr += "and s.name = :specialityName ";
        }

        if (rating.isPresent()) {
            queryStr +=
                    "and (select avg(review_value) from users_reviews where target_user_id = u.id group by target_user_id) >= :rating ";
        }

        if (sortBy.isPresent()) {
            val sortingType = PostsSortingType.fromName(sortBy.get());

            if (sortingType == PostsSortingType.NEWEST) {
                queryStr += "order by p.created_at desc ";
            } else if (sortingType == PostsSortingType.OLDEST) {
                queryStr += "order by p.created_at asc ";
            }
        } else {
            queryStr += "order by p.created_at desc ";
        }

        val query =
                session.createSQLQuery(queryStr)
                        .addScalar("id", LongType.INSTANCE)
                        .setParameter("lowestPrice", lowestPrice)
                        .setParameter("highestPrice", highestPrice)
                        .setParameter("userRoleId", UserRole.TEACHER.getId());

        if (q.isPresent()) {
            query.setParameter("q", "%" + q.get() + "%");
        }

        if (speciality.isPresent()) {
            query.setParameter("specialityName", speciality.get());
        }

        if (rating.isPresent()) {
            query.setParameter("rating", rating.get());
        }

        return (List<Long>) query.list();
    }
}
