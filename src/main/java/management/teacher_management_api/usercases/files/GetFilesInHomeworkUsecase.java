package management.teacher_management_api.usercases.files;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.payloads.UploadFileResponse;
import management.teacher_management_api.ports.persistence.HomeworksUploadsDao;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetFilesInHomeworkUsecase {
    private final HomeworksUploadsDao homeworksUploadsDao;

    public List<UploadFileResponse> execute(long userId, long homeworkId) {
        val uploads = homeworksUploadsDao.listFiles(homeworkId);

        return uploads.stream()
                .map(
                        homeworkUpload -> {
                            val fileDownloadUri =
                                    ServletUriComponentsBuilder.fromCurrentContextPath()
                                            .path("/files/homeworks/" + homeworkId + "/download/")
                                            .path(homeworkUpload.getFileName())
                                            .toUriString();
                            return UploadFileResponse.builder()
                                    .fileName(homeworkUpload.getFileName())
                                    .fileType(homeworkUpload.getFileType())
                                    .size(homeworkUpload.getFileSize())
                                    .fileDownloadUri(fileDownloadUri)
                                    .build();
                        })
                .collect(Collectors.toList());
    }
}
