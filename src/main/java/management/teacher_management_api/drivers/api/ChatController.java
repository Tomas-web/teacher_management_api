package management.teacher_management_api.drivers.api;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.converters.UserResolver;
import management.teacher_management_api.drivers.api.payloads.chat.ChatMessage;
import management.teacher_management_api.drivers.api.payloads.chat.Conversation;
import management.teacher_management_api.drivers.api.payloads.users.User;
import management.teacher_management_api.usercases.chat.GetConversationsUsecase;
import management.teacher_management_api.usercases.chat.MarkConversationSeenUsecase;
import management.teacher_management_api.usercases.chat.SearchTeachersForConversationUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final UserResolver userResolver;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GetConversationsUsecase getConversationsUsecase;
    private final MarkConversationSeenUsecase markConversationSeenUsecase;
    private final SearchTeachersForConversationUsecase searchTeachersForConversationUsecase;

    @MessageMapping("/private-message")
    public ChatMessage chat(@Payload ChatMessage message) {
        simpMessagingTemplate.convertAndSendToUser(
                message.getReceiverId(), "/private", message); // /user/userId/private
        return message;
    }

    @GetMapping(path = "/chat/conversations")
    public ResponseEntity<List<Conversation>> getConversations() {
        val userId = ApiUtils.getAuthenticatedUserId();

        val result = getConversationsUsecase.execute(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/chat/teachers/search")
    public ResponseEntity<List<User>> searchTeachers(@RequestParam(name = "q") String q) {
        val userId = ApiUtils.getAuthenticatedUserId();

        val result = searchTeachersForConversationUsecase.execute(userId, q);
        return ResponseEntity.ok(
                result.stream().map(userResolver::toDTO).collect(Collectors.toList()));
    }

    @PostMapping(path = "/chat/conversations/{conversation_id}/mark-seen")
    public ResponseEntity<Void> markConversationSeen(
            @PathVariable("conversation_id") String conversationId) {
        val userId = ApiUtils.getAuthenticatedUserId();

        markConversationSeenUsecase.execute(Long.parseLong(conversationId));
        return ResponseEntity.ok().build();
    }
}
