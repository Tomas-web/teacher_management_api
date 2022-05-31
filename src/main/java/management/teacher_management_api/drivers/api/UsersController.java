package management.teacher_management_api.drivers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.drivers.api.converters.UserResolver;
import management.teacher_management_api.drivers.api.converters.UserReviewResolver;
import management.teacher_management_api.drivers.api.exceptions.BadRequestException;
import management.teacher_management_api.drivers.api.exceptions.ConflictException;
import management.teacher_management_api.drivers.api.exceptions.ForbiddenException;
import management.teacher_management_api.drivers.api.payloads.LessonReservation;
import management.teacher_management_api.drivers.api.payloads.LessonReservationRequest;
import management.teacher_management_api.drivers.api.payloads.LessonReservationsResponse;
import management.teacher_management_api.drivers.api.payloads.SaveUserReviewRequest;
import management.teacher_management_api.drivers.api.payloads.users.AssignHomeworkRequest;
import management.teacher_management_api.drivers.api.payloads.users.GetUserWorkingTimesResponse;
import management.teacher_management_api.drivers.api.payloads.users.User;
import management.teacher_management_api.drivers.api.payloads.users.UserReview;
import management.teacher_management_api.usercases.users.GetLessonReservationsUsecase;
import management.teacher_management_api.usercases.users.GetReviewsUsecase;
import management.teacher_management_api.usercases.users.GetUserDataUsecase;
import management.teacher_management_api.usercases.users.GetUserWorkingTImesUsecase;
import management.teacher_management_api.usercases.users.SaveHomeworkUsecase;
import management.teacher_management_api.usercases.users.SaveUserReservationUsecase;
import management.teacher_management_api.usercases.users.SaveUserReviewUsecase;
import management.teacher_management_api.utils.DateTimeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@CrossOrigin
@Controller
@RequestMapping(value = "/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserReviewResolver userReviewResolver;
    private final GetReviewsUsecase getReviewsUsecase;
    private final SaveUserReviewUsecase saveUserReviewUsecase;
    private final GetUserDataUsecase getUserDataUsecase;
    private final UserResolver userResolver;
    private final GetUserWorkingTImesUsecase getUserWorkingTImesUsecase;
    private final SaveUserReservationUsecase saveUserReservationUsecase;
    private final GetLessonReservationsUsecase getLessonReservationsUsecase;
    private final SaveHomeworkUsecase saveHomeworkUsecase;

    @GetMapping(path = "/{user_id}")
    public ResponseEntity<User> getUserData(@PathVariable("user_id") String userId) {
        try {
            val result = getUserDataUsecase.execute(userId);

            if (result == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(userResolver.toDTO(result));
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/{user_id}/reviews")
    public ResponseEntity<List<UserReview>> getUserReviews(@PathVariable("user_id") String userId) {
        try {
            val result = getReviewsUsecase.execute(userId);
            return ResponseEntity.ok(
                    result.stream()
                            .map(userReviewResolver::toUserReviewDTO)
                            .collect(Collectors.toList()));
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/{user_id}/reviews")
    public ResponseEntity<Void> saveUserReview(
            @PathVariable("user_id") String userId, @RequestBody SaveUserReviewRequest body) {
        val reviewerId = ApiUtils.getAuthenticatedUserId();
        try {
            saveUserReviewUsecase.execute(reviewerId, userId, body.getComment(), body.getValue());
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/{user_id}/working-times")
    public ResponseEntity<GetUserWorkingTimesResponse> getUserWorkingTimes(
            @PathVariable("user_id") String teacherUid) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            val result = getUserWorkingTImesUsecase.execute(teacherUid);

            return ResponseEntity.ok(
                    GetUserWorkingTimesResponse.builder()
                            .timeStart(DateTimeUtils.toOffsetTime(result.getTimeStart()))
                            .timeEnd(DateTimeUtils.toOffsetTime(result.getTimeEnd()))
                            .build());
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/{user_id}/reservations")
    public ResponseEntity<LessonReservationsResponse> getUserReservations(
            @PathVariable("user_id") String teacherUid) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            val result = getLessonReservationsUsecase.execute(userId, Long.parseLong(teacherUid));
            return ResponseEntity.ok(
                    LessonReservationsResponse.builder()
                            .lessonReservations(
                                    result.stream()
                                            .map(
                                                    lessonReservation ->
                                                            LessonReservation.builder()
                                                                    .teacherId(
                                                                            Long.toString(
                                                                                    lessonReservation
                                                                                            .getTeacherId()))
                                                                    .studentId(
                                                                            Long.toString(
                                                                                    lessonReservation
                                                                                            .getStudentId()))
                                                                    .lessonStart(
                                                                            DateTimeUtils
                                                                                    .toOffsetDateTime(
                                                                                            lessonReservation
                                                                                                    .getLessonStart()))
                                                                    .lessonEnd(
                                                                            DateTimeUtils
                                                                                    .toOffsetDateTime(
                                                                                            lessonReservation
                                                                                                    .getLessonEnd()))
                                                                    .build())
                                            .collect(Collectors.toList()))
                            .build());
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/{user_id}/reservations")
    public ResponseEntity<Void> saveDateTimeReservation(
            @PathVariable("user_id") String teacherUid,
            @RequestBody LessonReservationRequest body) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            saveUserReservationUsecase.execute(userId, teacherUid, body.getDateTime());
            return ResponseEntity.ok().build();
        } catch (ConflictException ex) {
            log.error("Reservation dateTime conflicts with other reservation", ex);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (BadRequestException ex) {
            log.error("Reservation time is not available", ex);
            return ResponseEntity.badRequest().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(path = "/{user_id}/homeworks")
    public ResponseEntity<Void> assignHomeworkForStudent(
            @PathVariable("user_id") long studentId, @RequestBody AssignHomeworkRequest body) {
        val teacherId = ApiUtils.getAuthenticatedUserId();

        try {
            saveHomeworkUsecase.execute(
                    teacherId, studentId, body.getContent(), body.getDeadline());
            return ResponseEntity.ok().build();
        } catch (ForbiddenException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
