package management.teacher_management_api.drivers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.drivers.api.payloads.calls.CreateCallRoomResponse;
import management.teacher_management_api.usercases.calls.CreateCallRoomUsecase;
import management.teacher_management_api.usercases.calls.DeleteCallRoomUsecase;
import management.teacher_management_api.usercases.calls.ValidateCallRoomUsecase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class CallController {
    private final CreateCallRoomUsecase createCallRoomUsecase;
    private final DeleteCallRoomUsecase deleteCallRoomUsecase;
    private final ValidateCallRoomUsecase validateCallRoomUsecase;

    @GetMapping("/users/{user_id}/call-room/validate")
    public ResponseEntity<Void> validateCallRoom(
            @PathVariable("user_id") String targetId,
            @RequestParam("token") String token,
            @RequestParam("channel_name") String channelName,
            @RequestParam("caller_id") String callerId) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            val result =
                    validateCallRoomUsecase.execute(
                            userId, Long.parseLong(callerId), Long.parseLong(targetId), token, channelName);

            if (!result) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/users/{user_id}/call-room")
    public ResponseEntity<CreateCallRoomResponse> createCallRoom(
            @PathVariable("user_id") String targetId) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            val result = createCallRoomUsecase.execute(userId, Long.parseLong(targetId));

            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/users/{user_id}/call-room")
    public ResponseEntity<Void> deleteCallRoom(@PathVariable("user_id") String targetId) {
        val userId = ApiUtils.getAuthenticatedUserId();

        try {
            deleteCallRoomUsecase.execute(userId, Long.parseLong(targetId));
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Unhandled exception", ex);
            return ResponseEntity.internalServerError().build();
        }
    }
}
