package management.teacher_management_api.ports.persistence;

import management.teacher_management_api.infrastructure.hibernate.entities.Post;

public interface PostsDao {
    Post findForUser(long userId);
    Post findById(long id);

    void delete(long userId);
    void deleteById(long userId, long id);
    void save(long userId, String title, String content);
}
