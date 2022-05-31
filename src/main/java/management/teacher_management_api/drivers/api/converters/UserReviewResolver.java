package management.teacher_management_api.drivers.api.converters;

import lombok.RequiredArgsConstructor;
import management.teacher_management_api.drivers.api.payloads.users.Reviewer;
import management.teacher_management_api.drivers.api.payloads.users.UserReview;
import management.teacher_management_api.utils.DateTimeUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReviewResolver {

    public UserReview toUserReviewDTO(
            management.teacher_management_api.usercases.dto.UserReview userReview) {
        return UserReview.builder()
                .id(Long.toString(userReview.getId()))
                .comment(userReview.getComment())
                .createdAt(DateTimeUtils.toOffsetDateTime(userReview.getCreatedAt()))
                .value(userReview.getValue())
                .reviewer(
                        Reviewer.builder()
                                .id(Long.toString(userReview.getReviewer().getId()))
                                .name(userReview.getReviewer().getName())
                                .avatarUrl(userReview.getReviewer().getAvatarUrl())
                                .build())
                .build();
    }
}
