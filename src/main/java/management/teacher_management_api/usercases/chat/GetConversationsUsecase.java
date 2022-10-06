package management.teacher_management_api.usercases.chat;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.payloads.chat.ChatUser;
import management.teacher_management_api.drivers.api.payloads.chat.Conversation;
import management.teacher_management_api.ports.persistence.ConversationsDao;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.utils.DateTimeUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetConversationsUsecase {
    private final ConversationsDao conversationsDao;
    private final UsersDao usersDao;

    public List<Conversation> execute(long userId) {
        val conversations = conversationsDao.listAll(userId);

        return conversations.stream()
                .map(
                        conversation -> {
                            val latestMessage =
                                    conversationsDao.getLatestMessage(conversation.getId());
                            val sender = usersDao.findById(conversation.getSenderId());
                            return Conversation.builder()
                                    .id(Long.toString(conversation.getId()))
                                    .user(
                                            ChatUser.builder()
                                                    .id(Long.toString(sender.getId()))
                                                    .name(sender.getFullName())
                                                    .avatarUrl(sender.getPicture())
                                                    .build())
                                    .latestMessage(latestMessage.getMessage())
                                    .sentAt(
                                            DateTimeUtils.toOffsetDateTime(
                                                    latestMessage.getCreatedAt()))
                                    .isSeen(conversation.isSeen())
                                    .build();
                        })
                .collect(Collectors.toList());
    }
}
