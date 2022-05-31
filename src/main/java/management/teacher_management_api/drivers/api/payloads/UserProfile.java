package management.teacher_management_api.drivers.api.payloads;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetTime;

@Data
@Builder
public class UserProfile {
    private final String id;
    private final String fullName;
    private final String email;
    private final int roleId;
    private final String picture;
    private final String description;
    private final Double price;
    private final String address;
    private final String contacts;
    private final String speciality;
    private final OffsetTime workingTimeStart;
    private final OffsetTime workingTimeEnd;
}
