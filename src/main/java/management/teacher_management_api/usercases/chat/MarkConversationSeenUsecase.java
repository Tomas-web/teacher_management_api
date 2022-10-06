package management.teacher_management_api.usercases.chat;

import lombok.RequiredArgsConstructor;
import management.teacher_management_api.ports.persistence.ConversationsDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarkConversationSeenUsecase {
    private final ConversationsDao conversationsDao;

    public void execute(long conversationId) {
        conversationsDao.markSeen(conversationId);
    }
}
