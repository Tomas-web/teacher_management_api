package management.teacher_management_api.drivers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.domain.services.FileStorageService;
import management.teacher_management_api.drivers.api.exceptions.NotFoundException;
import management.teacher_management_api.drivers.api.payloads.UploadFileResponse;
import management.teacher_management_api.usercases.files.DeleteUploadedFileUsecase;
import management.teacher_management_api.usercases.files.UploadFileUsecase;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(path = "/files")
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;
    private final UploadFileUsecase uploadFileUsecase;
    private final DeleteUploadedFileUsecase deleteUploadedFileUsecase;

    @PostMapping("/upload")
    public UploadFileResponse uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("homework_id") String homeworkId) {
        val userId = ApiUtils.getAuthenticatedUserId();
        return uploadFileUsecase.execute(userId, Long.parseLong(homeworkId), file);
    }

    @PostMapping("/homeworks/{homework_id}/upload-multiple")
    public List<UploadFileResponse> uploadMultipleFiles(
            @PathVariable("homework_id") String homeworkId,
            @RequestParam("files") MultipartFile[] files) {
        val userId = ApiUtils.getAuthenticatedUserId();

        return Arrays.asList(files).stream()
                .map(file -> uploadFileUsecase.execute(userId, Long.parseLong(homeworkId), file))
                .collect(Collectors.toList());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFile(
            @RequestParam("homework_id") String homeworkId,
            @RequestParam("file_name") String fileName) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            deleteUploadedFileUsecase.execute(userId, Long.parseLong(homeworkId), fileName);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/homeworks/{homework_id}/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("homework_id") String homeworkId,
            @PathVariable String fileName,
            HttpServletRequest request) {
        val userId = ApiUtils.getAuthenticatedUserId();
        try {
            // Load file as Resource
            Resource resource =
                    fileStorageService.loadFileAsResource(Long.parseLong(homeworkId), fileName);

            // Try to determine file's content type
            String contentType = null;
            try {
                contentType =
                        request.getServletContext()
                                .getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                log.info("Could not determine file type.");
            }

            // Fallback to the default content type if type could not be determined
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (NotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
