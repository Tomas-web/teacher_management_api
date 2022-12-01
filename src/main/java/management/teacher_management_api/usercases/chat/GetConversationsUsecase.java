package management.teacher_management_api.usercases.chat;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.payloads.chat.CallRoomDetails;
import management.teacher_management_api.drivers.api.payloads.chat.ChatUser;
import management.teacher_management_api.drivers.api.payloads.chat.Conversation;
import management.teacher_management_api.ports.persistence.CallRoomsDao;
import management.teacher_management_api.ports.persistence.ConversationsDao;
import management.teacher_management_api.ports.persistence.ConversationsMessagesDao;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetConversationsUsecase {
    private final ConversationsDao conversationsDao;
    private final ConversationsMessagesDao conversationsMessagesDao;
    private final UsersDao usersDao;
    private final CallRoomsDao callRoomsDao;

    public List<Conversation> execute(long userId) {
        val conversations = conversationsDao.listAll(userId);

        return conversations.stream()
                .map(
                        conversation -> {
                            val latestMessage =
                                    conversationsMessagesDao.getLatestMessage(conversation.getId());
                            val sender =
                                    usersDao.findById(
                                            conversationsDao.getSecondParticipantId(
                                                    conversation.getId(), userId));

                            val callRoom = callRoomsDao.find(sender.getId(), userId);
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
                                                            .callerId(
                                                                    Long.toString(
                                                                            callRoom.getCallerId()))
                                                            .token(callRoom.getToken())
                                                            .channelName(callRoom.getName())
                                                            .build()
                                                    : null)
                                    .latestMessage(latestMessage.getMessage())
                                    .sentAt(
                                            DateTimeUtils.toOffsetDateTime(
                                                    latestMessage.getCreatedAt()))
                                    .isSeen(conversationsDao.isSeen(conversation.getId(), userId))
                                    .build();
                        })
                .collect(Collectors.toList());
    }
}
