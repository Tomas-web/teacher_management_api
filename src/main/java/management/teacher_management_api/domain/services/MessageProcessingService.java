package management.teacher_management_api.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.payloads.chat.ChatMessage;
import management.teacher_management_api.drivers.api.payloads.chat.ChatMessagePayload;
import management.teacher_management_api.drivers.api.payloads.chat.ChatUser;
import management.teacher_management_api.infrastructure.hibernate.entities.Conversation;
import management.teacher_management_api.ports.persistence.ConversationsDao;
import management.teacher_management_api.ports.persistence.ConversationsMessagesDao;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProcessingService {
    private final ConversationsDao conversationsDao;
    private final ConversationsMessagesDao conversationsMessagesDao;
    private final UsersDao usersDao;

    public ChatMessage execute(ChatMessagePayload message) {
        val receiverId = Long.parseLong(message.getReceiverId());
        val senderId = Long.parseLong(message.getSenderId());
        Conversation conversation = conversationsDao.find(receiverId, senderId);

        if (conversation == null) {
            conversation = conversationsDao.create(receiverId, senderId);
        }

        val conversationMessage =
                conversationsMessagesDao.saveMessage(
                        conversation.getId(), senderId, message.getMessage());

        conversationsDao.markUnseen(conversation.getId(), receiverId);

        val sender = usersDao.findById(conversationMessage.getSenderId());
        return ChatMessage.builder()
                .conversationId(Long.toString(conversation.getId()))
                .sentAt(DateTimeUtils.toOffsetDateTime(conversationMessage.getCreatedAt()))
                .message(conversationMessage.getMessage())
                .sender(
                        ChatUser.builder()
                                .id(Long.toString(sender.getId()))
                                .name(sender.getFullName())
                                .avatarUrl(sender.getPicture())
                                .build())
                .build();
    }
}
