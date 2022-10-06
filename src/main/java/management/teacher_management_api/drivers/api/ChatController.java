package management.teacher_management_api.drivers.api;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.payloads.chat.ChatMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/private-message")
    public ChatMessage chat(@Payload ChatMessage message) {
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverId(), "/private", message); // /user/userId/private
        return message;
    }

    @GetMapping(path = "/conversations")
    public ResponseEntity<Void> getConversations() {
        val userId = ApiUtils.getAuthenticatedUserId();

        return ResponseEntity.ok().build();
    }
}
