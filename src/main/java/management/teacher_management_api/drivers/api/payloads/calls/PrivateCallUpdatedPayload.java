package management.teacher_management_api.drivers.api.payloads.calls;

import lombok.Data;

@Data
public class PrivateCallUpdatedPayload {
    private String callerId;
    private String targetId;
    private String token;
    private String channelName;
    private String status;
}
