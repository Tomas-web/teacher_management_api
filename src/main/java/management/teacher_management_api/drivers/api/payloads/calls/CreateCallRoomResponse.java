package management.teacher_management_api.drivers.api.payloads.calls;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCallRoomResponse {
    private final String token;
    private final String channelName;
}
