package management.teacher_management_api.usercases.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.domain.core.UserRole;
import management.teacher_management_api.drivers.api.payloads.UpdateProfileRequest;
import management.teacher_management_api.ports.persistence.SpecialitiesDao;
import management.teacher_management_api.ports.persistence.UsersWorkingTimesDao;
import management.teacher_management_api.ports.persistence.UsersDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateProfileUsecase {
  private final UsersDao usersDao;
  private final SpecialitiesDao specialitiesDao;
  private final UsersWorkingTimesDao usersWorkingTimesDao;

  public void execute(long userId, UpdateProfileRequest data) {
    val user = usersDao.findById(userId);

    user.setFullName(data.getFullName());
    user.setEmail(data.getEmail());
    user.setDescription(data.getAbout());
    user.setRoleId(data.getRoleId());

    if (data.getRoleId() == UserRole.TEACHER.getId()) {
      user.setPrice(data.getPrice());
      user.setAddress(data.getAddress());
      user.setContacts(data.getContacts());
      user.setSpecialityId(specialitiesDao.findByName(data.getSpeciality()));

      val timeStart = usersWorkingTimesDao.getTimeStart(userId);

      if (timeStart == null) {
        usersWorkingTimesDao.saveWorkingTimes(userId, data.getWorkingTimeStart(), data.getWorkingTimeEnd());
      } else {
        usersWorkingTimesDao.updateTimeStart(userId, data.getWorkingTimeStart());
        usersWorkingTimesDao.updateTimeEnd(userId, data.getWorkingTimeEnd());
      }
    }

    user.setUpdatedAt(LocalDateTime.now());

    usersDao.saveOrUpdate(user);
  }
}
