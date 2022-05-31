package management.teacher_management_api.usercases.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.domain.core.UserRole;
import management.teacher_management_api.ports.persistence.UsersDao;
import management.teacher_management_api.ports.persistence.UsersWorkingTimesDao;
import management.teacher_management_api.services.auth.UserProfileService;
import management.teacher_management_api.usercases.dto.UserProfile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserProfileUsecase {
    private final UserProfileService userProfilesService;
    private final UsersDao usersDao;
    private final UsersWorkingTimesDao usersWorkingTimesDao;

    public UserProfile execute(String authToken) {
        try {
            val profile = userProfilesService.ensure(authToken);

            if (profile == null) {
                return null;
            }

            return UserProfile.builder()
                    .id(profile.getId())
                    .fullName(profile.getFullName())
                    .email(profile.getEmail())
                    .role(UserRole.fromId(profile.getRoleId()))
                    .picture(profile.getPicture())
                    .description(profile.getDescription())
                    .price(profile.getPrice())
                    .address(profile.getAddress())
                    .contacts(profile.getContacts())
                    .speciality(usersDao.getSpeciality(profile.getId()))
                    .workingTimeStart(usersWorkingTimesDao.getTimeStart(profile.getId()))
                    .workingTimeEnd(usersWorkingTimesDao.getTimeEnd(profile.getId()))
                    .build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            throw ex;
        }
    }
}
