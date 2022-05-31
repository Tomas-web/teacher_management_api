package management.teacher_management_api.usercases.users;

import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.core.UserRole;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.usercases.dto.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetStudentsUsecase {
    private final UsersDao usersDao;

    public List<User> execute(long teacherId, Optional<String> q) {
        val result =
                usersDao.listStudents(teacherId).stream()
                        .filter(user -> user.getId() != teacherId)
                        .map(
                                user ->
                                        User.builder()
                                                .id(user.getId())
                                                .fullName(user.getFullName())
                                                .email(user.getEmail())
                                                .picture(user.getPicture())
                                                .speciality(user.getSpecialityId() != null ?
                                                        usersDao.getSpeciality(
                                                                user.getSpecialityId()) : null)
                                                .contacts(user.getContacts())
                                                .address(user.getAddress())
                                                .price(user.getPrice())
                                                .role(UserRole.fromId(user.getRoleId()))
                                                .description(user.getDescription())
                                                .build())
                        .collect(Collectors.toList());

        if (q.isPresent()) {
            return result.stream()
                    .filter(
                            user ->
                                    user.getFullName()
                                            .toLowerCase()
                                            .contains(q.get().toLowerCase()))
                    .collect(Collectors.toList());
        }

        return result;
    }
}
