package management.teacher_management_api.drivers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.domain.core.HomeworkListType;
import management.teacher_management_api.drivers.api.converters.PostResolver;
import management.teacher_management_api.drivers.api.converters.UserResolver;
import management.teacher_management_api.drivers.api.exceptions.BadRequestException;
import management.teacher_management_api.drivers.api.payloads.GetPostViewsResponse;
import management.teacher_management_api.drivers.api.payloads.GetStudentHomeworksResponse;
import management.teacher_management_api.drivers.api.payloads.GetTeacherHomeworksResponse;
import management.teacher_management_api.drivers.api.payloads.Homework;
import management.teacher_management_api.drivers.api.payloads.LessonReservation;
import management.teacher_management_api.drivers.api.payloads.StudentLessonReservationsResponse;
import management.teacher_management_api.drivers.api.payloads.UpdateProfileRequest;
import management.teacher_management_api.drivers.api.payloads.UserProfile;
import management.teacher_management_api.drivers.api.payloads.posts.GetProfilePostResponse;
import management.teacher_management_api.drivers.api.payloads.posts.PostView;
import management.teacher_management_api.drivers.api.payloads.users.GetStudentsResponse;
import management.teacher_management_api.usercases.posts.GetUserPostUsecase;
import management.teacher_management_api.usercases.profile.GetProfileLessonReservationsUsecase;
import management.teacher_management_api.usercases.profile.GetStudentHomeworksUsecase;
import management.teacher_management_api.usercases.profile.GetTeacherHomeworksUsecase;
import management.teacher_management_api.usercases.profile.GetUserPostViewsUsecase;
import management.teacher_management_api.usercases.profile.GetUserProfileUsecase;
import management.teacher_management_api.usercases.profile.UpdateProfileUsecase;
import management.teacher_management_api.usercases.profile.UploadUserAvatarUseCase;
import management.teacher_management_api.usercases.users.GetStudentsUsecase;
import management.teacher_management_api.utils.DateTimeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final GetUserProfileUsecase getUserProfileUsecase;
    private final UpdateProfileUsecase updateProfileUsecase;
    private final UploadUserAvatarUseCase uploadUserAvatarUseCase;
    private final GetProfileLessonReservationsUsecase getProfileLessonReservationsUsecase;
    private final GetUserPostUsecase getUserPostUsecase;
    private final PostResolver postResolver;
    private final GetUserPostViewsUsecase getUserPostViewsUsecase;
    private final GetTeacherHomeworksUsecase getTeacherHomeworksUsecase;
    private final GetStudentHomeworksUsecase getStudentHomeworksUsecase;
    private final UserResolver userResolver;
    private final GetStudentsUsecase getStudentsUsecase;

    @GetMapping(path = "/profile")
    public ResponseEntity<UserProfile> getProfile(HttpServletRequest request) {
        val auth = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) auth.getCredentials();

        if (StringUtils.isEmpty(token)) {
            String a = request.getHeader(HttpHeaders.AUTHORIZATION);
            log.debug("token is empty; header = {}", a);

            if (!StringUtils.isEmpty(a)) {
                token = a.replace("Bearer ", "");
            }
        }

        try {
            val profile = getUserProfileUsecase.execute(token);

            if (profile == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            } else {
                return ResponseEntity.ok(
                        UserProfile.builder()
                                .id(Long.toString(profile.getId()))
                                .fullName(profile.getFullName())
                                .email(profile.getEmail())
                                .roleId(profile.getRole().getId())
                                .picture(profile.getPicture())
                                .description(profile.getDescription())
                                .price(profile.getPrice())
                                .address(profile.getAddress())
                                .contacts(profile.getContacts())
                                .speciality(profile.getSpeciality())
                                .workingTimeStart(
                                        DateTimeUtils.toOffsetTime(profile.getWorkingTimeStart()))
                                .workingTimeEnd(
                                        DateTimeUtils.toOffsetTime(profile.getWorkingTimeEnd()))
                                .build());
            }
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(path = "/profile")
    public ResponseEntity<Void> updateProfile(@RequestBody UpdateProfileRequest body) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            updateProfileUsecase.execute(userId, body);

            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/profile/picture")
    public ResponseEntity<Void> uploadPicture(@RequestParam("image") MultipartFile file) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            uploadUserAvatarUseCase.execute(userId, file.getBytes());

            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/profile/lesson-reservations")
    public ResponseEntity<StudentLessonReservationsResponse> getLessonReservations() {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            val result = getProfileLessonReservationsUsecase.execute(userId);
            return ResponseEntity.ok(
                    StudentLessonReservationsResponse.builder()
                            .lessonReservations(
                                    result.stream()
                                            .map(
                                                    r ->
                                                            LessonReservation.builder()
                                                                    .studentId(
                                                                            Long.toString(
                                                                                    r
                                                                                            .getStudentId()))
                                                                    .teacherId(
                                                                            Long.toString(
                                                                                    r
                                                                                            .getTeacherId()))
                                                                    .lessonStart(
                                                                            DateTimeUtils
                                                                                    .toOffsetDateTime(
                                                                                            r
                                                                                                    .getLessonStart()))
                                                                    .lessonEnd(
                                                                            DateTimeUtils
                                                                                    .toOffsetDateTime(
                                                                                            r
                                                                                                    .getLessonEnd()))
                                                                    .build())
                                            .collect(Collectors.toList()))
                            .build());
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/profile/post")
    public ResponseEntity<GetProfilePostResponse> getProfilePost() {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            val result = getUserPostUsecase.execute(userId);

            return ResponseEntity.ok(
                    GetProfilePostResponse.builder()
                            .post(result != null ? postResolver.toDTO(result) : null)
                            .build());
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/profile/post-views")
    public ResponseEntity<GetPostViewsResponse> getProfilePostViews(
            @RequestParam(value = "period") String period) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            val result = getUserPostViewsUsecase.execute(userId, period);

            return ResponseEntity.ok(
                    GetPostViewsResponse.builder()
                            .postViews(
                                    result.stream()
                                            .map(
                                                    review ->
                                                            PostView.builder()
                                                                    .value(review.getValue())
                                                                    .date(
                                                                            DateTimeUtils
                                                                                    .toOffsetDateTime(
                                                                                            review
                                                                                                    .getDate()))
                                                                    .build())
                                            .collect(Collectors.toList()))
                            .build());
        } catch (BadRequestException ex) {
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/profile/homeworks/teacher")
    public ResponseEntity<GetTeacherHomeworksResponse> getHomeworksForTeacher(
            @RequestParam(value = "list_type") String listType) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            val type = HomeworkListType.fromName(listType);

            if (type == null) {
                return ResponseEntity.badRequest().build();
            }

            val result = getTeacherHomeworksUsecase.execute(userId, type);

            return ResponseEntity.ok(
                    GetTeacherHomeworksResponse.builder()
                            .homeworks(
                                    result.stream()
                                            .map(
                                                    homework ->
                                                            Homework.builder()
                                                                    .id(
                                                                            Long.toString(
                                                                                    homework
                                                                                            .getId()))
                                                                    .teacher(
                                                                            userResolver
                                                                                    .toUserRefDTO(
                                                                                            homework
                                                                                                    .getTeacherId()))
                                                                    .student(
                                                                            userResolver
                                                                                    .toUserRefDTO(
                                                                                            homework
                                                                                                    .getStudentId()))
                                                                    .content(homework.getContent())
                                                                    .deadline(
                                                                            DateTimeUtils
                                                                                    .toOffsetDateTime(
                                                                                            homework
                                                                                                    .getDeadline()))
                                                                    .build())
                                            .collect(Collectors.toList()))
                            .build());
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/profile/homeworks/student")
    public ResponseEntity<GetStudentHomeworksResponse> getHomeworksForStudent(
            @RequestParam(value = "list_type") String listType) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            val type = HomeworkListType.fromName(listType);

            if (type == null) {
                return ResponseEntity.badRequest().build();
            }

            val result = getStudentHomeworksUsecase.execute(userId, type);

            return ResponseEntity.ok(
                    GetStudentHomeworksResponse.builder()
                            .homeworks(
                                    result.stream()
                                            .map(
                                                    homework ->
                                                            Homework.builder()
                                                                    .id(
                                                                            Long.toString(
                                                                                    homework
                                                                                            .getId()))
                                                                    .teacher(
                                                                            userResolver
                                                                                    .toUserRefDTO(
                                                                                            homework
                                                                                                    .getTeacherId()))
                                                                    .student(
                                                                            userResolver
                                                                                    .toUserRefDTO(
                                                                                            homework
                                                                                                    .getStudentId()))
                                                                    .content(homework.getContent())
                                                                    .deadline(
                                                                            DateTimeUtils
                                                                                    .toOffsetDateTime(
                                                                                            homework
                                                                                                    .getDeadline()))
                                                                    .build())
                                            .collect(Collectors.toList()))
                            .build());
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/profile/students")
    public ResponseEntity<GetStudentsResponse> getStudents(
            @RequestParam(value = "q") Optional<String> q) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            val result = getStudentsUsecase.execute(userId, q);

            return ResponseEntity.ok(
                    GetStudentsResponse.builder()
                            .students(
                                    result.stream()
                                            .map(userResolver::toDTO)
                                            .collect(Collectors.toList()))
                            .build());
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
