package management.teacher_management_api.ports.persistence;

import management.teacher_management_api.domain.core.User;

import java.util.List;

public interface UsersDao {
    User findById(long userId);

    User findByExternalId(String externalId);

    List<User> findTeachersWithoutConversationStarted(long userId, String q);

    String getSpeciality(long userId);

    Double getPrice(long userId);

    List<User> listStudents(long userId);

    boolean isTeacher(long userId);

    void updatePicture(long userId, String url);

    void saveOrUpdate(User user);
}
