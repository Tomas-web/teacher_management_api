package management.teacher_management_api.drivers.api.payloads.chat;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class Conversation {
    private final String id;
    private final ChatUser user;
    private final String latestMessage;
    private final boolean isSeen;
    private final OffsetDateTime sentAt;
}
