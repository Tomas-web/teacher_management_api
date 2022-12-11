package management.teacher_management_api.drivers.api.payloads;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadFileResponse {
    private final String fileName;
    private final String fileDownloadUri;
    private final String fileType;
    private final long size;
}
