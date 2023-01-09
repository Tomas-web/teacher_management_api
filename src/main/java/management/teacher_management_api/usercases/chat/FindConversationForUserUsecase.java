package management.teacher_management_api.usercases.chat;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.payloads.chat.CallRoomDetails;
import management.teacher_management_api.drivers.api.payloads.chat.ChatUser;
import management.teacher_management_api.drivers.api.payloads.chat.Conversation;
import management.teacher_management_api.infrastructure.hibernate.entities.CallRoom;
import management.teacher_management_api.ports.persistence.CallRoomsDao;
import management.teacher_management_api.ports.persistence.ConversationsDao;
import management.teacher_management_api.ports.persistence.ConversationsMessagesDao;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindConversationForUserUsecase {
    private final ConversationsDao conversationsDao;
    private final UsersDao usersDao;
    private final CallRoomsDao callRoomsDao;
    private final ConversationsMessagesDao conversationsMessagesDao;

    public Conversation execute(long userId, long senderId) {
        val conversation = conversationsDao.find(userId, senderId);
        val latestMessage = conversationsMessagesDao.getLatestMessage(conversation.getId());
        val sender = usersDao.findById(senderId);

        CallRoom callRoom = callRoomsDao.find(sender.getId(), userId);

        if (callRoom == null) {
            callRoom = callRoomsDao.find(userId, sender.getId());
        }
        return Conversation.builder()
                .id(Long.toString(conversation.getId()))
                .user(
                        ChatUser.builder()
                                .id(Long.toString(sender.getId()))
                                .name(sender.getFullName())
                                .avatarUrl(sender.getPicture())
                                .build())
                .callRoomDetails(
                        callRoom != null
                                ? CallRoomDetails.builder()
                                        .callerId(Long.toString(callRoom.getCallerId()))
                                        .channelName(callRoom.getName())
                                        .token(callRoom.getToken())
                                        .build()
                                : null)
                .latestMessage(latestMessage.getMessage())
                .sentAt(DateTimeUtils.toOffsetDateTime(latestMessage.getCreatedAt()))
                .isSeen(conversationsDao.isSeen(conversation.getId(), userId))
                .build();
    }
}
