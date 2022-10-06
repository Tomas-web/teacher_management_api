package management.teacher_management_api.drivers.api.payloads.chat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatUser {
    private final String id;
    private final String name;
    private final String avatarUrl;
}
