package management.teacher_management_api.usercases.chat;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.payloads.chat.ChatUser;
import management.teacher_management_api.drivers.api.payloads.chat.Conversation;
import management.teacher_management_api.ports.persistence.ConversationsDao;
import management.teacher_management_api.ports.persistence.ConversationsMessagesDao;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetConversationUsecase {
    private final ConversationsDao conversationsDao;
    private final ConversationsMessagesDao conversationsMessagesDao;
    private final UsersDao usersDao;

    public Conversation execute(long conversationId, long userId) {
        val latestMessage = conversationsMessagesDao.getLatestMessage(conversationId);
        val sender =
                usersDao.findById(conversationsDao.getSecondParticipantId(conversationId, userId));
        return Conversation.builder()
                .id(Long.toString(conversationId))
                .user(
                        ChatUser.builder()
                                .id(Long.toString(sender.getId()))
                                .name(sender.getFullName())
                                .avatarUrl(sender.getPicture())
                                .build())
                .latestMessage(latestMessage.getMessage())
                .sentAt(DateTimeUtils.toOffsetDateTime(latestMessage.getCreatedAt()))
                .isSeen(conversationsDao.isSeen(conversationId, userId))
                .build();
    }
}
