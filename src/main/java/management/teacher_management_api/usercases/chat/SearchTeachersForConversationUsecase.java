package management.teacher_management_api.usercases.chat;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.core.UserRole;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.usercases.dto.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchTeachersForConversationUsecase {
    private final UsersDao usersDao;

    public List<User> execute(long userId, String q) {
        val users = usersDao.findTeachersWithoutConversationStarted(userId, q);

        return users.stream()
                .map(
                        user ->
                                User.builder()
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
                                        .build())
                .collect(Collectors.toList());
    }
}
