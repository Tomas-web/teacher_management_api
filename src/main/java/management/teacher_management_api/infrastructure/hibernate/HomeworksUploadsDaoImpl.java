package management.teacher_management_api.infrastructure.hibernate;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.infrastructure.hibernate.entities.HomeworkUpload;
import management.teacher_management_api.ports.persistence.HomeworksUploadsDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class HomeworksUploadsDaoImpl implements HomeworksUploadsDao {
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public HomeworkUpload find(long homeworkId, long userId, String fileName) {
        val session = Utils.currentSession(entityManagerFactory);

        return (HomeworkUpload)
                session.createSQLQuery(
                                "select * from homeworks_uploads where homework_id = :homeworkId and user_id = :userId and file_name = :fileName limit 1")
                        .addEntity(HomeworkUpload.class)
                        .setParameter("homeworkId", homeworkId)
                        .setParameter("userId", userId)
                        .setParameter("fileName", fileName)
                        .uniqueResult();
    }

    @Override
    public HomeworkUpload findByName(long homeworkId, String fileName) {
        val session = Utils.currentSession(entityManagerFactory);

        return (HomeworkUpload)
                session.createSQLQuery(
                                "select * from homeworks_uploads where homework_id = :homeworkId and file_name = :fileName limit 1")
                        .addEntity(HomeworkUpload.class)
                        .setParameter("homeworkId", homeworkId)
                        .setParameter("fileName", fileName)
                        .uniqueResult();
    }

    @Override
    public List<HomeworkUpload> listFiles(long homeworkId) {
        val session = Utils.currentSession(entityManagerFactory);

        return (List<HomeworkUpload>)
                session.createSQLQuery(
                                "select * from homeworks_uploads where homework_id = :homeworkId")
                        .addEntity(HomeworkUpload.class)
                        .setParameter("homeworkId", homeworkId)
                        .list();
    }

    @Override
    public void delete(long homeworkId, long userId, String fileName) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "delete from homeworks_uploads where homework_id = :homeworkId and user_id = :userId and file_name = :fileName")
                .setParameter("homeworkId", homeworkId)
                .setParameter("userId", userId)
                .setParameter("fileName", fileName)
                .executeUpdate();
    }

    @Override
    public void save(long homeworkId, long userId, String fileName, String fileType, long fileSize) {
        val session = Utils.currentSession(entityManagerFactory);

        session.createSQLQuery(
                        "insert into homeworks_uploads(homework_id, user_id, file_name, file_size, file_type, uploaded_at) values(:homeworkId, :userId, :fileName, :fileSize, :fileType, :uploadedAt)")
                .setParameter("homeworkId", homeworkId)
                .setParameter("userId", userId)
                .setParameter("fileName", fileName)
                .setParameter("fileSize", fileSize)
                .setParameter("fileType", fileType)
                .setParameter("uploadedAt", LocalDateTime.now())
                .executeUpdate();
    }
}
