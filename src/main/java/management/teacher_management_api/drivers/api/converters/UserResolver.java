package management.teacher_management_api.drivers.api.converters;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.drivers.api.payloads.UserRef;
import management.teacher_management_api.drivers.api.payloads.users.User;
import management.teacher_management_api.ports.persistence.UsersDao;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserResolver {
    private final UsersDao usersDao;

    public User toDTO(management.teacher_management_api.usercases.dto.User user) {
        return User.builder()
                .id(Long.toString(user.getId()))
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roleId(user.getRole().getId())
                .picture(user.getPicture())
                .description(user.getDescription())
                .price(user.getPrice())
                .address(user.getAddress())
                .contacts(user.getContacts())
                .speciality(user.getSpeciality())
                .build();
    }

    public UserRef toUserRefDTO(long userId) {
        val user = usersDao.findById(userId);

        return UserRef.builder()
                .id(Long.toString(userId))
                .name(user.getFullName())
                .avatarUrl(user.getPicture())
                .build();
    }
}
