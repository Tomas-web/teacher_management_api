package management.teacher_management_api.usercases.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HomeworkUpload {
    private final String fileName;
    private final String fileType;
    private final long fileSize;
    private final String downloadUri;
    private final LocalDateTime uploadedAt;
}
