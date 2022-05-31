package management.teacher_management_api.usercases.profile;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.core.Period;
import management.teacher_management_api.drivers.api.exceptions.BadRequestException;
import management.teacher_management_api.ports.persistence.PostViewsDao;
import management.teacher_management_api.usercases.dto.PostView;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetUserPostViewsUsecase {
    private final PostViewsDao postViewsDao;

    public List<PostView> execute(long userId, String periodStr) {
        val period = Period.fromName(periodStr);

        if (period == null) {
            throw new BadRequestException();
        }

        val result = new ArrayList<PostView>();

        val baselineDate = LocalDate.now().atStartOfDay().plusDays(1);
        val numOfDays = period.getNumOfDays();
        val periodStep = getPeriodStep(numOfDays);

        for (int i = 0; i < numOfDays; i += periodStep) {
            val periodStart = baselineDate.minusDays(numOfDays - i);
            val periodEnd = periodStart.plusDays(periodStep);
            val views = postViewsDao.findForUserInPeriod(userId, periodStart, periodEnd);

            result.add(
                    PostView.builder()
                            .value(views.isEmpty() ? 0 : views.get(0).getNumOfViews())
                            .date(periodStart)
                            .build());
        }

        return result;
    }

    private int getPeriodStep(int numOfDays) {
        if (numOfDays <= 30) {
            return 1;
        } else if (numOfDays <= 180) {
            return 7;
        } else {
            return 30;
        }
    }
}
