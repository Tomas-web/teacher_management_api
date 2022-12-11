package management.teacher_management_api.usercases.files;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.services.FileStorageService;
import management.teacher_management_api.drivers.api.payloads.UploadFileResponse;
import management.teacher_management_api.ports.persistence.HomeworksUploadsDao;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UploadFileUsecase {
    private final FileStorageService fileStorageService;
    private final HomeworksUploadsDao homeworksUploadsDao;

    public UploadFileResponse execute(long userId, long homeworkId, MultipartFile file) {
        val fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        val uploadedFile =
                homeworksUploadsDao.find(
                        homeworkId,
                        userId,
                        fileName);

        if (uploadedFile != null) {
            homeworksUploadsDao.delete(homeworkId, userId, fileName);
        }

        fileStorageService.storeFile(userId, homeworkId, file);

        homeworksUploadsDao.save(homeworkId, userId, fileName, file.getContentType(), file.getSize());

        String fileDownloadUri =
                ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/files/homeworks/" + homeworkId + "/download/")
                        .path(fileName)
                        .toUriString();

        return UploadFileResponse.builder()
                .fileName(fileName)
                .fileDownloadUri(fileDownloadUri)
                .size(file.getSize())
                .fileType(file.getContentType())
                .build();
    }
}
