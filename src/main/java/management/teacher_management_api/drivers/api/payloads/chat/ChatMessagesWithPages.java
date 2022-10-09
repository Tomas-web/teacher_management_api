package management.teacher_management_api.drivers.api.payloads.chat;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatMessagesWithPages {
    private final int totalPages;
    private final List<ChatMessage> messages;
}
