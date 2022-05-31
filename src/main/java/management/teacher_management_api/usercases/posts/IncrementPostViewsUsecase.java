package management.teacher_management_api.usercases.posts;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.ports.persistence.PostViewsDao;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class IncrementPostViewsUsecase {
    private final PostViewsDao postViewsDao;

    public void execute(long postId) {
        val now = LocalDate.now();
        val periodStart = now.atStartOfDay();
        val periodEnd = periodStart.plusDays(1);

        val postViews = postViewsDao.findInPeriod(postId, periodStart, periodEnd);

        if (postViews.isEmpty()) {
            postViewsDao.save(postId, 0, periodStart);
            return;
        }

        postViewsDao.update(postId, postViews.get(0).getNumOfViews() + 1, periodStart, periodEnd);
    }
}
