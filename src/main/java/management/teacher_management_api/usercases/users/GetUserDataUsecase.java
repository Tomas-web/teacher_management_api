package management.teacher_management_api.usercases.users;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.core.UserRole;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.usercases.dto.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserDataUsecase {
    private final UsersDao usersDao;

    public User execute(String userUid) {
        val userId = Long.parseLong(userUid);

        val user = usersDao.findById(userId);

        if (user == null) {
            return null;
        }

        return User.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(UserRole.fromId(user.getRoleId()))
                .picture(user.getPicture())
                .description(user.getDescription())
                .price(user.getPrice())
                .address(user.getAddress())
                .contacts(user.getContacts())
                .speciality(usersDao.getSpeciality(user.getId()))
                .build();
    }
}
