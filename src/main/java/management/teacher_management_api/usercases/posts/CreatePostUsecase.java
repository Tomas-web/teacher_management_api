package management.teacher_management_api.usercases.posts;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.ports.persistence.PostsDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatePostUsecase {
    private final PostsDao postsDao;

    public void execute(long userId, String title, String content) {
        postsDao.delete(userId);
        postsDao.save(userId, title, content);
    }
}
