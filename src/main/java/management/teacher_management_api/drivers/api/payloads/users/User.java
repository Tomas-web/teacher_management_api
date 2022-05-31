package management.teacher_management_api.drivers.api.payloads.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
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
}
