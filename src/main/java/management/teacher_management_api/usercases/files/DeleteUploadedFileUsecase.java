package management.teacher_management_api.usercases.files;

import lombok.RequiredArgsConstructor;
import management.teacher_management_api.domain.services.FileStorageService;
import management.teacher_management_api.ports.persistence.HomeworksUploadsDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUploadedFileUsecase {
    private final HomeworksUploadsDao homeworksUploadsDao;
    private final FileStorageService fileStorageService;

    public void execute(long userId, long homeworkId, String fileName) {
        fileStorageService.deleteFile(userId, homeworkId, fileName);
        homeworksUploadsDao.delete(homeworkId, userId, fileName);
    }
}
