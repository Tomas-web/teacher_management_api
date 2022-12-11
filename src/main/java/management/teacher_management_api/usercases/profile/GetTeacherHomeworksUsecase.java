package management.teacher_management_api.usercases.profile;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.core.HomeworkListType;
import management.teacher_management_api.ports.persistence.HomeworksUploadsDao;
import management.teacher_management_api.ports.persistence.UsersHomeworksDao;
import management.teacher_management_api.usercases.dto.Homework;
import management.teacher_management_api.usercases.dto.HomeworkUpload;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetTeacherHomeworksUsecase {
    private final UsersHomeworksDao usersHomeworksDao;
    private final HomeworksUploadsDao homeworksUploadsDao;

    public List<Homework> execute(long teacherId, HomeworkListType type) {
        val homeworks = usersHomeworksDao.findForTeacher(teacherId);

        val result =
                homeworks.stream()
                        .map(
                                userHomework -> {
                                    val files = homeworksUploadsDao.listFiles(userHomework.getId());
                                    return Homework.builder()
                                            .id(userHomework.getId())
                                            .teacherId(userHomework.getTeacherId())
                                            .studentId(userHomework.getStudentId())
                                            .content(userHomework.getContent())
                                            .deadline(userHomework.getDeadline())
                                            .uploads(
                                                    files.stream()
                                                            .map(
                                                                    file -> {
                                                                        val fileDownloadUri =
                                                                                ServletUriComponentsBuilder
                                                                                        .fromCurrentContextPath()
                                                                                        .path(
                                                                                                "/files/homeworks/"
                                                                                                        + userHomework
                                                                                                                .getId()
                                                                                                        + "/download/")
                                                                                        .path(
                                                                                                file
                                                                                                        .getFileName())
                                                                                        .toUriString();
                                                                        return HomeworkUpload
                                                                                .builder()
                                                                                .fileName(
                                                                                        file
                                                                                                .getFileName())
                                                                                .fileSize(
                                                                                        file
                                                                                                .getFileSize())
                                                                                .uploadedAt(
                                                                                        file
                                                                                                .getUploadedAt())
                                                                                .downloadUri(
                                                                                        fileDownloadUri)
                                                                                .fileType(
                                                                                        file
                                                                                                .getFileType())
                                                                                .build();
                                                                    })
                                                            .collect(Collectors.toList()))
                                            .build();
                                })
                        .collect(Collectors.toList());

        if (type == HomeworkListType.UPCOMING) {
            val now = LocalDateTime.now();
            return result.stream()
                    .filter(homework -> homework.getDeadline().isAfter(now))
                    .collect(Collectors.toList());
        }

        return result;
    }
}
