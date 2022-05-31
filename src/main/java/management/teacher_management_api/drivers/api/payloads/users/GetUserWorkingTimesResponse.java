package management.teacher_management_api.drivers.api.payloads.users;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetTime;

@Data
@Builder
public class GetUserWorkingTimesResponse {
    private final OffsetTime timeStart;
    private final OffsetTime timeEnd;
}
