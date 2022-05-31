package management.teacher_management_api.drivers.api.payloads;

import lombok.Data;

@Data
public class SaveUserReviewRequest {
    private int value;
    private String comment;
}
