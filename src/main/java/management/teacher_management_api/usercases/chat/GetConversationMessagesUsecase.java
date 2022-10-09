package management.teacher_management_api.usercases.chat;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.payloads.chat.ChatMessage;
import management.teacher_management_api.drivers.api.payloads.chat.ChatMessagesWithPages;
import management.teacher_management_api.drivers.api.payloads.chat.ChatUser;
import management.teacher_management_api.ports.persistence.ConversationsMessagesDao;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetConversationMessagesUsecase {
    private static final long MAX_MESSAGES_NUM = 20;

    private final ConversationsMessagesDao conversationsMessagesDao;
    private final UsersDao usersDao;

    public ChatMessagesWithPages execute(long userId, long conversationId, int pageNumber) {
        val messages = conversationsMessagesDao.listForConversation(conversationId);

        return ChatMessagesWithPages.builder()
                .totalPages((int) Math.ceil((double) messages.size() / (double) MAX_MESSAGES_NUM))
                .messages(
                        messages.stream()
                                .skip((pageNumber - 1) * MAX_MESSAGES_NUM)
                                .limit(MAX_MESSAGES_NUM)
                                .map(
                                        message -> {
                                            val sender = usersDao.findById(message.getSenderId());

                                            return ChatMessage.builder()
                                                    .conversationId(Long.toString(conversationId))
                                                    .message(message.getMessage())
                                                    .sender(
                                                            ChatUser.builder()
                                                                    .id(
                                                                            Long.toString(
                                                                                    sender.getId()))
                                                                    .name(sender.getFullName())
                                                                    .avatarUrl(sender.getPicture())
                                                                    .build())
                                                    .sentAt(
                                                            DateTimeUtils.toOffsetDateTime(
                                                                    message.getCreatedAt()))
                                                    .build();
                                        })
                                .collect(Collectors.toList()))
                .build();
    }
}
