package management.teacher_management_api.domain.core;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExternalUserInfo {
    private final String id;
    private final String displayName;
    private final String email;
    private final String avatarUrl;
}
