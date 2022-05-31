package management.teacher_management_api.usercases.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.ports.persistence.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@Service
public class UploadUserAvatarUseCase {
    private final UsersDao usersDao;
    private final String avatarsFolder;
    private final String avatarsBasePath;
    private final String dirName;

    @Autowired
    public UploadUserAvatarUseCase(
            UsersDao usersDao,
            @Value("${management.dashboard.avatars.path}") String avatarsFolder,
            @Value("${management.dashboard.avatars.url}") String avatarsBasePath,
            @Value("${management.dashboard.avatars.dir}") String dirName) {
        this.dirName = dirName;
        this.usersDao = usersDao;
        this.avatarsFolder = avatarsFolder;
        this.avatarsBasePath = avatarsBasePath;
    }


    @Transactional
    public void execute(long userId, byte[] file) throws IOException {
        log.info("Updating profile picture of user id={}", userId);

        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        val fileName = Paths.get(uploadPath, String.format("user_%d.jpg", userId));
        Files.deleteIfExists(fileName);

        Files.write(fileName, file, StandardOpenOption.CREATE_NEW);

        usersDao.updatePicture(
                userId, String.format("%s/temp/%s", avatarsBasePath, String.format("user_%d.jpg", userId)));
    }
}
