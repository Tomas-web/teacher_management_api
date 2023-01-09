package management.teacher_management_api.drivers.api.payloads.calls;

import lombok.Data;

@Data
public class ValidateCallRoomRequest {
    private String token;
    private String channelName;
    private String callerId;
}
