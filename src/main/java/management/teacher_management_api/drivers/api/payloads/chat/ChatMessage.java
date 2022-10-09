package management.teacher_management_api.drivers.api.payloads.chat;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class ChatMessage {
    private final String conversationId;
    private final ChatUser sender;
    private final String message;
    private final OffsetDateTime sentAt;
}
