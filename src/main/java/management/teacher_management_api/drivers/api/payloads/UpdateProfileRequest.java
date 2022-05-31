package management.teacher_management_api.drivers.api.payloads;

import lombok.Data;

import java.time.LocalTime;

@Data
public class UpdateProfileRequest {
  private String fullName;
  private String email;
  private String about;
  private int roleId;
  private Double price;
  private String address;
  private String contacts;
  private String speciality;
  private LocalTime workingTimeStart;
  private LocalTime workingTimeEnd;
}
