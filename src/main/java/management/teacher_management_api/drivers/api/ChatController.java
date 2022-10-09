package management.teacher_management_api.drivers.api;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.services.MessageProcessingService;
import management.teacher_management_api.drivers.api.converters.UserResolver;
import management.teacher_management_api.drivers.api.payloads.chat.ChatMessagePayload;
import management.teacher_management_api.drivers.api.payloads.chat.ChatMessagesWithPages;
import management.teacher_management_api.drivers.api.payloads.chat.Conversation;
import management.teacher_management_api.drivers.api.payloads.users.User;
import management.teacher_management_api.usercases.chat.GetConversationMessagesUsecase;
import management.teacher_management_api.usercases.chat.GetConversationUsecase;
import management.teacher_management_api.usercases.chat.GetConversationsUsecase;
import management.teacher_management_api.usercases.chat.MarkConversationSeenUsecase;
import management.teacher_management_api.usercases.chat.SearchTeachersForConversationUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@CrossOrigin
@RequiredArgsConstructor
public class ChatController {
    private final UserResolver userResolver;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final GetConversationsUsecase getConversationsUsecase;
    private final MarkConversationSeenUsecase markConversationSeenUsecase;
    private final SearchTeachersForConversationUsecase searchTeachersForConversationUsecase;
    private final GetConversationMessagesUsecase getConversationMessagesUsecase;
    private final MessageProcessingService messageProcessingService;
    private final GetConversationUsecase getConversationUsecase;

    @MessageMapping("/private-message")
    public ChatMessagePayload chat(@Payload ChatMessagePayload message) {
        val newMessage = messageProcessingService.execute(message);
        simpMessagingTemplate.convertAndSendToUser(
                message.getReceiverId(), "/private", newMessage); // /user/receiverId/private
        simpMessagingTemplate.convertAndSendToUser(
                message.getSenderId(), "/private", newMessage); // /user/senderId/private
        return message;
    }

    @GetMapping(path = "/chat/conversations")
    public ResponseEntity<List<Conversation>> getConversations() {
        val userId = ApiUtils.getAuthenticatedUserId();

        val result = getConversationsUsecase.execute(userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/chat/conversations/{conversation_id}")
    public ResponseEntity<Conversation> getConversation(
            @PathVariable("conversation_id") String conversationId) {
        val userId = ApiUtils.getAuthenticatedUserId();

        val result = getConversationUsecase.execute(Long.parseLong(conversationId), userId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(path = "/chat/teachers/search")
    public ResponseEntity<List<User>> searchTeachers(@RequestParam(name = "q") String q) {
        val userId = ApiUtils.getAuthenticatedUserId();

        val result = searchTeachersForConversationUsecase.execute(userId, q);
        return ResponseEntity.ok(
                result.stream().map(userResolver::toDTO).collect(Collectors.toList()));
    }

    @GetMapping(path = "/chat/conversations/{conversation_id}/messages")
    public ResponseEntity<ChatMessagesWithPages> getConversationMessages(
            @PathVariable("conversation_id") String conversationId,
            @RequestParam(name = "page") int pageNumber) {
        val userId = ApiUtils.getAuthenticatedUserId();

        val result =
                getConversationMessagesUsecase.execute(
                        userId, Long.parseLong(conversationId), pageNumber);

        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/chat/conversations/{conversation_id}/mark-seen")
    public ResponseEntity<Void> markConversationSeen(
            @PathVariable("conversation_id") String conversationId) {
        val userId = ApiUtils.getAuthenticatedUserId();

        markConversationSeenUsecase.execute(Long.parseLong(conversationId), userId);
        return ResponseEntity.ok().build();
    }
}
