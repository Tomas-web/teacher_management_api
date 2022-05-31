package management.teacher_management_api.usercases.dto;

import lombok.Builder;
import lombok.Data;
import management.teacher_management_api.domain.core.UserRole;

@Data
@Builder
public class User {
    private final long id;
    private final String fullName;
    private final String email;
    private final UserRole role;
    private final String picture;
    private final String description;
    private final Double price;
    private final String address;
    private final String contacts;
    private final String speciality;
}
