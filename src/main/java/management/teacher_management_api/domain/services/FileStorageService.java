package management.teacher_management_api.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.drivers.api.exceptions.FileStorageException;
import management.teacher_management_api.drivers.api.exceptions.NotFoundException;
import management.teacher_management_api.infrastructure.spring.FileStorageProperties;
import management.teacher_management_api.ports.persistence.HomeworksUploadsDao;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {
    private final FileStorageProperties fileStorageProperties;
    private final HomeworksUploadsDao homeworksUploadsDao;

    public String storeFile(long userId, long homeworkId, MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
                throw new FileStorageException(
                        "Sorry! Filename contains invalid path sequence " + fileName);
            }

            val fileStorageLocation = getFileStorageLocation(userId, homeworkId);
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException(
                    "Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public void deleteFile(long userId, long homeworkId, String fileName) {
        try {
            val fileStorageLocation = getFileStorageLocation(userId, homeworkId);
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.delete(targetLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not delete file with name " + fileName, ex);
        }
    }

    public Resource loadFileAsResource(long homeworkId, String fileName) {
        val homeworkUpload = homeworksUploadsDao.findByName(homeworkId, fileName);

        if (homeworkUpload == null) {
            log.error("File not found {}", fileName);
            throw new NotFoundException();
        }

        val fileStorageLocation = getFileStorageLocation(homeworkUpload.getUserId(), homeworkId);

        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                log.error("File not found {}", fileName);
                throw new NotFoundException();
            }
        } catch (MalformedURLException ex) {
            log.error("File not found {}", fileName, ex);
            throw new NotFoundException();
        }
    }

    private Path getFileStorageLocation(long userId, long homeworkId) {
        val pathString =
                fileStorageProperties.getUploadDir()
                        + "\\homework"
                        + homeworkId
                        + "\\user"
                        + userId;
        val fileStorageLocation = Paths.get(pathString).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
            return fileStorageLocation;
        } catch (Exception ex) {
            throw new FileStorageException(
                    "Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
}
