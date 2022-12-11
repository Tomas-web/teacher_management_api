package management.teacher_management_api.drivers.api.payloads;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class HomeworkUpload {
    private final String fileName;
    private final String fileType;
    private final long fileSize;
    private final String downloadUri;
    private final OffsetDateTime uploadedAt;
}
