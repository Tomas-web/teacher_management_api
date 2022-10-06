package management.teacher_management_api.drivers.api.payloads.chat;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ChatMessage {
    private String senderId;
    private String receiverId;
    private String message;
    private OffsetDateTime date;
}
