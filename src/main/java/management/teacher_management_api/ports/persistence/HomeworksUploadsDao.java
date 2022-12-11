package management.teacher_management_api.ports.persistence;

import management.teacher_management_api.infrastructure.hibernate.entities.HomeworkUpload;

import java.util.List;

public interface HomeworksUploadsDao {
    HomeworkUpload find(long homeworkId, long userId, String fileName);
    List<HomeworkUpload> listFiles(long homeworkId);
    void delete(long homeworkId, long userId, String fileName);
    void save(long homeworkId, long userId, String fileName, String fileType, long fileSize);
}
