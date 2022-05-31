package management.teacher_management_api.usercases.posts;

import lombok.RequiredArgsConstructor;
import management.teacher_management_api.ports.persistence.PostsDao;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePostUsecase {
    private final PostsDao postsDao;

    public void execute(long userId, long postId) {
        postsDao.deleteById(userId, postId);
    }
}
